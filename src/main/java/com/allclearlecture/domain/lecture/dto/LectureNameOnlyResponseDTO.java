package com.allclearlecture.domain.lecture.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
@Builder
public class LectureNameOnlyResponseDTO {
    private String lectureName;

    public LectureNameOnlyResponseDTO(String lectureName) {
        this.lectureName = lectureName;
    }

    public static LectureNameOnlyResponseDTO fromLectureName(String lectureName) {
        if (lectureName == null) {
            return null;
        }
        return LectureNameOnlyResponseDTO.builder()
                .lectureName(lectureName)
                .build();
    }
}
