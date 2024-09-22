package com.allclearlecture.domain.registration.service;

import com.allclearlecture.domain.lecture.entity.Lecture;
import com.allclearlecture.domain.lecture.repository.LectureRepository;
import com.allclearlecture.domain.registration.entity.Registration;
import com.allclearlecture.domain.registration.exception.CourseAlreadyFulledException;
import com.allclearlecture.domain.registration.exception.SubjectAlreadyRegisteredException;
import com.allclearlecture.domain.registration.repository.RegistrationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.allclearlecture.domain.registration.enums.RegistrationErrorCode.*;

@Service
@RequiredArgsConstructor
@Transactional
public class RegistrationCommandService {

    private final RegistrationRepository registrationRepository;
    private final LectureRepository lectureRepository;

    //수강 신청
    public void createRegistration(Long studentId, Long lectureId) {
        final Lecture lecture = lectureRepository.findById(lectureId).get();

        // 신청 가능한지 여러 조건 확인
        checkIfSubjectAlreadyRegistered(studentId, lecture.getId());
        checkCourseLimitation(lecture);
        // TODO 신청 학점 체크
        checkCreditLimit(lecture);

        // 수강신청 저장
        final Registration newRegistration = Registration.builder()
                .studentId(studentId)
                .lecture(lecture)
                .build();
        registrationRepository.save(newRegistration);

        // 현재 수강 신청 인원 증가
        lecture.addRegistration();

        // TODO 신청 학점 증가
    }

    // 이미 신청한 교과목인지 확인
    private void checkIfSubjectAlreadyRegistered(Long studentId, Long lectureId) {
        boolean registered = registrationRepository.existsByStudentIdAndLectureId(studentId, lectureId);
        if (registered) {
            throw new SubjectAlreadyRegisteredException(SUBJECT_ALREADY_REGISTERED);
        }
    }

    // 수강 정원 이내 인지 확인
    private static void checkCourseLimitation(Lecture lecture) {
        if (lecture.getCurrentNumberOfStudents() >= lecture.getAllowedNumberOfStudents()) {
            throw new CourseAlreadyFulledException(COURSE_ALREADY_FULLED);
        }
    }

    // TODO 이수가능학점이내 여부 확인
    private static void checkCreditLimit(Lecture lecture) {
//        if (student.getPossibleCredits() < student.getAppliedCredits() + lecture.getCurrentNumberOfStudents()) {
//            throw new CreditExceededException(CREDIT_EXCEEDED);
//        }
    }
}
