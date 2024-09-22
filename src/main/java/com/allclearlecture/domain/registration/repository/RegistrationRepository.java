package com.allclearlecture.domain.registration.repository;

import com.allclearlecture.domain.registration.entity.Registration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RegistrationRepository extends JpaRepository<Registration, Long> {
    boolean existsByStudentIdAndLectureId(Long studentId, Long lectureId);
    List<Registration> findByStudentId(Long studentId);
    Optional<Registration> findByIdAndStudentId(Long registrationId, Long studentId);
}
