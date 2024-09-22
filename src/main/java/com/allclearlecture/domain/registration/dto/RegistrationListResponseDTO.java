package com.allclearlecture.domain.registration.dto;

import com.allclearlecture.domain.registration.entity.Registration;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class RegistrationListResponseDTO {
    private final List<RegistrationResponseDTO> response;

    public static RegistrationListResponseDTO from(final List<Registration> registrations) {
        return new RegistrationListResponseDTO(
                registrations.stream()
                .map(RegistrationResponseDTO::from)
                .toList()
        );
    }
}
