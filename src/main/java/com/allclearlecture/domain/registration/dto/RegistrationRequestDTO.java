package com.allclearlecture.domain.registration.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationRequestDTO {
    private String lectureCode;
    private String division;
}
