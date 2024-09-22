package com.allclearlecture.domain.wishlist.service;

import com.allclearlecture.domain.lecture.entity.Lecture;
import com.allclearlecture.domain.lecture.repository.LectureRepository;
import com.allclearlecture.domain.wishlist.entity.Wishlist;
import com.allclearlecture.domain.wishlist.repository.WishlistRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WishlistService {

    /*private final AmazonSQS amazonSQS;*/
    /*private final SQSService sqsService;*/
    private final WishlistRepository wishlistRepository;
    private final LectureRepository lectureRepository;

    @PersistenceContext
    private EntityManager entityManager;

    private final String queueUrl = "https://sqs.ap-northeast-2.amazonaws.com/851725245699/AllClear.fifo";

    private static final Logger logger = LoggerFactory.getLogger(WishlistService.class);
    private static final Long STUDENT_ID = 202345L;  // student_id 고정

    @Transactional(readOnly = true)
    public List<Wishlist> getLectureDetails() {
        List<Wishlist> wishlistItems = wishlistRepository.findByStudentId(STUDENT_ID);
        System.out.println("Wishlist Items: " + wishlistItems); // 로그 확인
        return wishlistItems;
    }

    @Transactional(readOnly = true)
    public List<Lecture> getWishlistLectures() {
        List<Wishlist> wishlistItems = wishlistRepository.findByStudentId(STUDENT_ID);
        // wishlistItems에서 Lecture 객체를 추출
        return wishlistItems.stream()
                .map(Wishlist::getLecture)
                .toList();
    }
    @Transactional(readOnly = true)
    public double getTotalCredits() {
        List<Wishlist> wishlistItems = wishlistRepository.findByStudentId(STUDENT_ID);
        return wishlistItems.stream()
                .mapToDouble(item -> item.getLecture().getCredit()) // Lecture 객체의 credit 속성 사용
                .sum();
    }
    @Transactional(readOnly = true)
    public double getTotalOcuCredits() {
        return getLectureDetails().stream()
                .mapToDouble(item -> {
                    String lectureCode = item.getLecture().getLectureCode();
                    if (lectureCode != null && lectureCode.startsWith("열린")) {
                        return item.getLecture().getCredit();
                    }
                    return 0;
                })
                .sum();
    }

    @Transactional
    public void updatePriorities(Long studentId, Map<String, String> priorities) {
        for (Map.Entry<String, String> entry : priorities.entrySet()) {
            try {
                String[] keyParts = entry.getKey().split("__");

                if (keyParts.length < 2) {
                    throw new IllegalArgumentException("Invalid key format: " + entry.getKey());
                }

                Long wishlistId = Long.parseLong(keyParts[1]); // Extract ID from the parameter name
                int priority = Integer.parseInt(entry.getValue());

                // Update database
                Wishlist wishlist = wishlistRepository.findById(wishlistId)
                        .orElseThrow(() -> new IllegalArgumentException("Wishlist item not found: " + wishlistId));
                wishlist.setPriority(priority);
                wishlistRepository.save(wishlist);

                // Create message payload
                String messageBody = String.format("{\"action\": \"updatePriority\", \"id\": %d, \"priority\": %d}", wishlistId, priority);

                // Send message to SQS
                /*SendMessageRequest sendMessageRequest = new SendMessageRequest()
                        .withQueueUrl(queueUrl)
                        .withMessageBody(messageBody)
                        .withMessageGroupId("wishlist") // For FIFO queue
                        .withMessageDeduplicationId(String.valueOf(wishlistId)); // Add deduplication ID

                amazonSQS.sendMessage(sendMessageRequest);*/
            } catch (IllegalArgumentException e) {
                // Log and handle error
                System.err.println("Error processing entry: " + entry + ". " + e.getMessage());
            }
        }
    }
    @Transactional(readOnly = true)
    public List<Wishlist> getLecturesSortedByPriority() {
        // 특정 studentId로 필터링하고, 우선순위로 정렬
        return wishlistRepository.findByStudentId(STUDENT_ID, Sort.by(Sort.Order.asc("priority")));
    }
    /*@Transactional
    public void deleteWishlist(Long id) {
        Wishlist wishlist = entityManager.find(Wishlist.class, id);
        if (wishlist != null) {
            entityManager.remove(wishlist);
            entityManager.flush(); // 강제로 플러시하여 캐시를 비웁니다.
        }
    }*/
    @Transactional
    public void deleteWishlist(Long id) {
        Wishlist wishlist = entityManager.find(Wishlist.class, id);
        if (wishlist != null) {
            entityManager.remove(wishlist);
            entityManager.flush(); // 강제로 플러시하여 캐시를 비웁니다.
        }

        String messageBody = createMessageForDeletion(id);
        String messageDeduplicationId = String.valueOf(id); // Use ID as deduplication ID
        String messageGroupId = "wishlist"; // Define the group ID

        /*try {
            sqsService.sendMessage(messageBody, messageDeduplicationId, messageGroupId);
        } catch (SdkClientException e) {
            logger.error("Error sending message to SQS: {}", e.getMessage());
            throw new RuntimeException("Failed to delete wishlist. Please check your AWS credentials and configuration.");
        } catch (Exception e) {
            logger.error("An unexpected error occurred while deleting the wishlist: {}", e.getMessage());
            throw new RuntimeException("An unexpected error occurred. Please try again later.");
        }*/
    }

    private String createMessageForDeletion(Long id) {
        return "Delete wishlist with ID: " + id;
    }

    @Transactional
    public void addLectureToWishlist(String lectureCode, String division, Long studentId) {
        List<Lecture> lectures = lectureRepository.findByLectureCodeAndDivision(lectureCode, division);

        if (!lectures.isEmpty()) {
            Lecture lecture = lectures.get(0); // 첫 번째 강의 선택 (필요에 따라 수정 가능)

            Wishlist wishlist = new Wishlist();
            wishlist.setLecture(lecture);
            wishlist.setStudentId(studentId);
            wishlist.setPriority(0);
            wishlistRepository.save(wishlist);

            String messageBody = String.format("Added lecture to wishlist: %s (%s)", lectureCode, division);
            String messageGroupId = "wishlist";
            String messageDeduplicationId = UUID.randomUUID().toString();
            /*sqsService.sendMessage(messageBody, messageDeduplicationId, messageGroupId);*/
        } else {
            throw new IllegalArgumentException("강의를 찾을 수 없습니다.");
        }
    }
}