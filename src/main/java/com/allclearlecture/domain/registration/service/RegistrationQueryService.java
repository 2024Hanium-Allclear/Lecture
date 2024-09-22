package com.allclearlecture.domain.registration.service;

import com.allclearlecture.domain.registration.dto.RegistrationListResponseDTO;
import com.allclearlecture.domain.registration.entity.Registration;
import com.allclearlecture.domain.registration.repository.RegistrationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RegistrationQueryService {

    private final RegistrationRepository registrationRepository;

    //수강신청 내역 조회
    public RegistrationListResponseDTO getRegistration(Long studentId) {
        final List<Registration> list = registrationRepository.findByStudentId(studentId);
        return RegistrationListResponseDTO.from(list);
    }
}
