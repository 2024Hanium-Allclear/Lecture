package com.allclearlecture.domain.registration.controller;

import com.allclearlecture.domain.registration.dto.RegistrationRequestDTO;
import com.allclearlecture.domain.registration.service.RegistrationCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/registration")
public class RegistrationController {

    private final RegistrationCommandService registrationCommandService;

    //수강 신청
    @PostMapping("/{lectureId}")
    public ResponseEntity<Void> register(@PathVariable Long lectureId) {
        //TODO 사용자 정보 받아오기
        registrationCommandService.createRegistration(1L, lectureId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    //빠른 수강신청
    @PostMapping("/quick")
    public ResponseEntity<Void> quickRegister(@RequestBody final RegistrationRequestDTO request) {
        //TODO 사용자 정보 받아오기
        registrationCommandService.createQuickRegistration(1L, request);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    //수강 취소
    @PutMapping("/{registrationId}")
    public ResponseEntity<Void> delete(@PathVariable Long registrationId) {
        //TODO 사용자 정보 받아오기
        registrationCommandService.deleteRegistration(registrationId, 1L);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
