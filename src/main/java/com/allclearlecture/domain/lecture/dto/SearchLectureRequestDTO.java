package com.allclearlecture.domain.lecture.dto;

import com.allclearlecture.domain.lecture.enums.SearchOption;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SearchLectureRequestDTO {
    private SearchOption searchOption;
    private String query;       // 검색어 (학과 이름, 키워드, 학수번호 등)
    private String grade;       // 학년 (옵션)
    private String lectureName;
    private String division; // 분반 (옵션)

    public SearchLectureRequestDTO(SearchOption searchOption, String query, String grade, String lectureName, String division) {
        this.searchOption = searchOption;
        this.query = query;
        this.grade = grade;
        this.lectureName = lectureName;
        this.division = division;
    }
}
