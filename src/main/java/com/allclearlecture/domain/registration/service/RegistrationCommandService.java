package com.allclearlecture.domain.registration.service;

import com.allclearlecture.domain.lecture.entity.Lecture;
import com.allclearlecture.domain.lecture.repository.LectureRepository;
import com.allclearlecture.domain.registration.dto.RegistrationRequestDTO;
import com.allclearlecture.domain.registration.entity.Registration;
import com.allclearlecture.domain.registration.exception.CourseAlreadyFulledException;
import com.allclearlecture.domain.registration.exception.CourseTimeConflictException;
import com.allclearlecture.domain.registration.exception.NoAuthorityToRegistrationException;
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

        final List<Registration> registrations = registrationRepository.findByStudentId(studentId);
        checkTimetable(lecture.getLectureTime(), registrations);

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

    //빠른 수강 신청
    public void createQuickRegistration(Long studentId, RegistrationRequestDTO requestDto) {
        final Lecture lecture = lectureRepository.findByLectureCodeAndDivision(requestDto.getLectureCode(), requestDto.getDivision()).get(0);
        final Registration newRegistration = Registration.builder()
                .studentId(studentId)
                .lecture(lecture)
                .build();
        registrationRepository.save(newRegistration);
    }

    //수강 취소
    public void deleteRegistration(Long registrationId, Long studentId) {
        // 신청한 학생이 맞는지 확인
        Registration registration = registrationRepository.findByIdAndStudentId(registrationId, studentId)
                .orElseThrow(() -> new NoAuthorityToRegistrationException(NO_AUTHORITY_TO_REGISTRATION));

        //수강신청 삭제
        registrationRepository.deleteById(registrationId);

        // 현재 수강 신청 인원 감소
        registration.getLecture().deleteRegistration();

        // TODO 신청 학점 감소
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

    // 현재 강의 시간이 신청가능한지 확인
    private void checkTimetable(String currentTimetable, List<Registration> registrations) {
        if (!registrations.isEmpty()) { // 수강신청 목록이 있다면
            // 강의 시간이 겹치는 지 확인
            StringBuilder sb = new StringBuilder();
            for (Registration r : registrations) {
                sb.append(r.getLecture().getLectureTime());
                sb.append(",");
            }
            int[] timetableOfCourse = makeIntTimetable(currentTimetable);
            int[] timetableOfStudent = makeIntTimetable(sb.toString());
            compareTimetable(timetableOfCourse, timetableOfStudent);
        }
    }

    // 비교할 배열로 만들기
    private int[] makeIntTimetable(String rawTimetable) {
        enum DayOfWeek { 월, 화, 수, 목, 금, 토, 일 }
        int[] timetable = new int[7];
        for (String t : rawTimetable.split(",")) {
            int i = DayOfWeek.valueOf(t.substring(0, 1)).ordinal();
            String[] periods = t.substring(2).split(" ");
            for (String period : periods) {
                // 비트마스크 사용해 period 번째 비트에 1표시
                int p = 1 << Integer.parseInt(period);
                timetable[i] |= p;
            }
        }
        return timetable;
    }

    // 시간표 비교
    private static void compareTimetable(int[] timetable1, int[] timetable2) {
        for (int i = 0; i < 7; i++) {
            if ((timetable1[i] & timetable2[i]) > 0) { // 겹치는게 있을 경우 &연산시 0보다 큰값이 나옴
                throw new CourseTimeConflictException(COURSE_TIME_CONFLICT);
            }
        }
    }
}
