package com.allclearlecture.domain.lecture.dto;

import com.allclearlecture.domain.lecture.entity.Lecture;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LectureResponseDTO {
    private String lectureName;
    private String division;
    private int credit;
    private int allowedNumberOfStudents;
    private int currentNumberOfStudents;
    private String lectureDayAndRoom;
    private String lectureClassification;  //이수구분
    private String lectureTime;

    public LectureResponseDTO(String lectureName, String division, int credit, int allowedNumberOfStudents,
                              int currentNumberOfStudents, String lectureDayAndRoom, String lectureClassification, String lectureTime) {
        this.lectureName = lectureName;
        this.division = division;
        this.credit = credit;
        this.allowedNumberOfStudents = allowedNumberOfStudents;
        this.currentNumberOfStudents = currentNumberOfStudents;
        this.lectureDayAndRoom = lectureDayAndRoom;
        this.lectureClassification = lectureClassification;
        this.lectureTime = lectureTime;
    }

    public static LectureResponseDTO fromEntity(Lecture lecture) {
        if (lecture == null) {
            return null;
        }

        return LectureResponseDTO.builder()
                .lectureName(lecture.getLectureName())
                .division(lecture.getDivision())
                .credit(lecture.getCredit())
                .allowedNumberOfStudents(lecture.getAllowedNumberOfStudents())
                .currentNumberOfStudents(lecture.getCurrentNumberOfStudents())
                .lectureDayAndRoom(lecture.getLectureDayAndRoom())
                .lectureClassification(lecture.getLectureClassification())
                .lectureTime(lecture.getLectureTime())
                .build();
    }
}
