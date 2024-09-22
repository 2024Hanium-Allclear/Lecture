package com.allclearlecture.domain.registration.service;

import com.allclearlecture.domain.lecture.entity.Lecture;
import com.allclearlecture.domain.lecture.repository.LectureRepository;
import com.allclearlecture.domain.registration.entity.Registration;
import com.allclearlecture.domain.registration.repository.RegistrationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class RegistrationCommandService {

    private final RegistrationRepository registrationRepository;
    private final LectureRepository lectureRepository;

    //수강 신청
    public void createRegistration(Long studentId, Long lectureId) {
        final Lecture lecture = lectureRepository.findById(lectureId).get();

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

}
