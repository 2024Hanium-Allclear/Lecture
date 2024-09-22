package com.allclearlecture.domain.registration.dto;

import com.allclearlecture.domain.registration.entity.Registration;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RegistrationResponseDTO {
    private final Long registrationId;
    private final String classification;
    private final String courseNumber;
    private final String classNumber;
    private final String name;
    private final int credit;
    private final String professor;
    private final String lectureDay;
    private final String reenrollment;
    private final String note;
    private final String isFirst;

    public static RegistrationResponseDTO from(final Registration registration) {
        return new RegistrationResponseDTO(
                registration.getId(),
                registration.getLecture().getLectureClassification(),
                registration.getLecture().getLectureCode(),
                registration.getLecture().getDivision(),
                registration.getLecture().getLectureName(),
                registration.getLecture().getCredit(),
                registration.getLecture().getProfessor().getName(),
                registration.getLecture().getLectureDayAndRoom(),
                "-",
                "",
                ""
        );
    }

}
