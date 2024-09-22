package com.allclearlecture.domain.fileupload.consumer;


import com.allclearlecture.domain.lecture.entity.Department;
import com.allclearlecture.domain.lecture.entity.Lecture;
import com.allclearlecture.domain.lecture.entity.Professor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileToDBConsumer<K extends Serializable, V extends Serializable> {
    public static final Logger logger = LoggerFactory.getLogger(FileToDBConsumer.class.getName());
    protected KafkaConsumer<K, V> kafkaConsumer;
    protected List<String> topics;

    private LecturesDBHandler lecturesDBHandler;


    public FileToDBConsumer(Properties consumerProps, List<String> topics, LecturesDBHandler lecturesDBHandler) {
        this.kafkaConsumer = new KafkaConsumer<>(consumerProps);
        this.topics = topics;
        this.lecturesDBHandler = lecturesDBHandler; //DB처리 업무처리용객체
    }
    public void startConsuming(String commitMode, long durationMillis) {
        initConsumer();
        pollConsumes(durationMillis, commitMode);
        close();
    }

    //Kafka Consumer를 초기화하고, 지정된 토픽을 구독
    public void initConsumer() {
        this.kafkaConsumer.subscribe(this.topics);
        shutdownHookToRuntime(this.kafkaConsumer);
    }

    //에플리케이션이 종료될 때 안전하게 KafkaConsumer를 종료하기 위해 wakeup()을 호출하는 스레드를 추가
    private void shutdownHookToRuntime(KafkaConsumer<K, V> kafkaConsumer) {
        Thread mainThread = Thread.currentThread();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.info("Main program starts to exit by calling wakeup");
            kafkaConsumer.wakeup();
            try {
                mainThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }));
    }


    private Lecture makeLecture(ConsumerRecord<K, V> record) {
        String messageValue = (String) record.value();
        // 키를 문자열로 변환 (필요한 경우)
        String key = record.key() != null ? record.key().toString() : "";

        logger.info("Received record with key: " + key + " ########## messageValue: " + messageValue);


        try {
            // 메시지를 올바른 인코딩으로 재인코딩 시도

            final String regex = "(?<=^|,)(\"(?:[^\"]|\"\")*\"|[^,]*)";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(messageValue);

            List<String> tokens = new ArrayList<>();
            while (matcher.find()) {
                tokens.add(matcher.group().replaceAll("^\"|\"$", "").replace("\"\"", "\""));
            }

            // 로그로 토큰 확인
            for (int i = 0; i < tokens.size(); i++) {
                logger.info("Token " + i + ": " + tokens.get(i).trim());
            }

            Long lectureId = tryParseLong(key.trim());
            Long departmentId = tryParseLong(tokens.get(0).trim());
            String lectureCode = tokens.get(1).trim();
            String lectureName = tokens.get(2).trim();
            String division = tokens.get(3).trim();
            String lecture_classification = tokens.get(4).trim();
            Long professorId = tryParseLong(tokens.get(5).trim());
            Integer credit = tryParseInt(tokens.get(6).trim());
            Integer allowedNumberOfStudents = tryParseInt(tokens.get(7).trim());
            Integer currentNumberOfStudents = tryParseInt(tokens.get(8).trim());
            String grade = tokens.get(9).trim();
            String lectureDayAndRoom = tokens.get(10).trim();
            String lectureTime = tokens.get(11).trim();
            Integer lectureYear = tryParseInt(tokens.get(12).trim());
            Integer semester = tryParseInt(tokens.get(13).trim());
            String syllabus = tokens.get(14).trim();

            if (lectureId == null || departmentId == null || professorId == null) {
                logger.error("Invalid data: " + messageValue);
                return null;
            }

            Professor professor = new Professor(professorId);
            Department department = new Department(departmentId);

            return Lecture.builder()
                    .id(lectureId)
                    .professor(professor)
                    .department(department)
                    .lectureCode(lectureCode)
                    .division(division)
                    .lectureClassification(lecture_classification)
                    .lectureName(lectureName)
                    .grade(grade)
                    .credit(credit != null ? credit : 0) // Default value if null
                    .allowedNumberOfStudents(allowedNumberOfStudents != null ? allowedNumberOfStudents : 0) // Default value if null
                    .currentNumberOfStudents(currentNumberOfStudents != null ? currentNumberOfStudents : 0) // Default value if null
                    .lectureDayAndRoom(lectureDayAndRoom)
                    .lectureTime(lectureTime)
                    .lectureYear(lectureYear != null ? lectureYear : 0)
                    .semester(semester != null ? semester : 0)
                    .syllabus(syllabus)
                    .delStatus(false)
                    .build();
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            logger.error("Error parsing message: " + messageValue, e);
            return null;
        }
    }
    private Long tryParseLong(String value) {
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            logger.error("Failed to parse Long from value: " + value, e);
            return null;
        }
    }

    private static Integer tryParseInt(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            logger.error("Failed to parse Integer from value: {}", value, e);
            return null;
        }
    }

    private void processRecords(ConsumerRecords<K, V> records) {
        List<Lecture> lectures = makeLectures(records);
        if (lectures != null && !lectures.isEmpty()) {
            try {
                for (Lecture lecture : lectures) {
                    processLectureRecord(lecture);
                    this.lecturesDBHandler.insertOrUpdateLecture(lecture);
                }
            } catch (Exception e) {
                logger.error("Failed to insert or update lectures in the database", e);
            }
        } else {
            logger.warn("No valid lectures to insert or update, or lectures list is empty");
        }
    }

    public void processLectureRecord(Lecture lecture) {
        LocalDateTime now = LocalDateTime.now();
        lecture.setCreatedDate(now);
        lecture.setModifiedDate(now);
    }


    private List<Lecture> makeLectures(ConsumerRecords<K, V> records) {
        List<Lecture> lectures = new ArrayList<>();
        for (ConsumerRecord<K, V> record : records) {
            Lecture lecture = makeLecture(record);
            if (lecture != null) {
                lectures.add(lecture);
            } else {
                logger.error("Failed to create Lecture from record: " + record.value());
            }
        }
        return lectures;
    }

    public void pollConsumes(long durationMillis, String commitMode) {
        if (commitMode.equals("sync")) {
            pollCommitSync(durationMillis);
        } else {
            pollCommitAsync(durationMillis);
        }
    }

    //Kafka로부터 메시지를 지속적으로 소비하는 루프를 실행
    //비동기식 커밋을 사용하여 성능을 높일 수 있으나, 오프셋 저장 실패 시 문제가 발생할 수 있음
    private void pollCommitAsync(long durationMillis) {
        try {
            while (true) {
                ConsumerRecords<K, V> consumerRecords = this.kafkaConsumer.poll(Duration.ofMillis(durationMillis));
                logger.info("consumerRecords count: " + consumerRecords.count());
                if (consumerRecords.count() > 0) {
                    try {
                        processRecords(consumerRecords);
                    } catch (Exception e) {
                        logger.error(e.getMessage());
                    }
                }
                this.kafkaConsumer.commitAsync((offsets, exception) -> {
                    if (exception != null) {
                        logger.error("Offsets {} not completed, error: {}", offsets, exception.getMessage());
                    }
                });
            }
        } catch (WakeupException e) {
            logger.error("Wakeup exception has been called", e);
        } catch (Exception e) {
            logger.error("Exception during polling: " + e.getMessage(), e);
        } finally {
            logger.info("##### Commit sync before closing");
            try {
                kafkaConsumer.commitSync();
            } catch (Exception e) {
                logger.error("Exception during commit sync: " + e.getMessage(), e);
            }
            logger.info("Finally consumer is closing");
            close();
        }
    }
    // 동기식으로 메시지를 소비하고 커밋하며, 예외가 발생해도 안전하게 종료
    protected void pollCommitSync(long durationMillis) {
        try {
            while (true) {
                ConsumerRecords<K, V> consumerRecords = this.kafkaConsumer.poll(Duration.ofMillis(durationMillis));
                processRecords(consumerRecords);
                if (consumerRecords.count() > 0) {
                    this.kafkaConsumer.commitSync();
                    logger.info("Commit sync has been called");
                }
            }
        } catch (WakeupException e) {
            logger.error("Wakeup exception has been called");
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            logger.info("##### Commit sync before closing");
            kafkaConsumer.commitSync();
            logger.info("Finally consumer is closing");
            close();
        }
    }

    protected void close() {
        this.kafkaConsumer.close();
        this.lecturesDBHandler.close();
    }

}
