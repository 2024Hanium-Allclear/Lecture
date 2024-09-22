package com.allclearlecture.domain.lecture.controller;
import com.allclearlecture.domain.lecture.dto.LectureNameOnlyResponseDTO;
import com.allclearlecture.domain.lecture.dto.LectureResponseDTO;
import com.allclearlecture.domain.lecture.dto.SearchLectureRequestDTO;
import com.allclearlecture.domain.lecture.enums.SearchOption;
import com.allclearlecture.domain.lecture.service.LectureQueryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lectures")
@CrossOrigin(origins = "*")
public class LectureController {
    private final LectureQueryService lectureQueryService;

    public LectureController(LectureQueryService lectureQueryService) {
        this.lectureQueryService = lectureQueryService;
    }

    // 강의 검색 - SearchOption에 따른 검색
    @GetMapping("/search")
    public ResponseEntity<List<LectureResponseDTO>> searchLectures(
            @RequestParam(value = "searchOption") SearchOption searchOption,
            @RequestParam(value = "query", required = false) String query,
            @RequestParam(value = "grade", required = false) String grade,
            @RequestParam(value = "lectureName", required = false) String lectureName,
            @RequestParam(value = "division", required = false) String division) {

        SearchLectureRequestDTO searchDTO = new SearchLectureRequestDTO(searchOption, query, grade, lectureName, division);
        List<LectureResponseDTO> lectures = lectureQueryService.searchLectures(searchDTO);
        return new ResponseEntity<>(lectures, HttpStatus.OK);
    }

    // 검색어를 입력하면 해당 이름을 가진 과목 이름만 조회
    @PostMapping("/search-by-keyword")
    public ResponseEntity<List<LectureNameOnlyResponseDTO>> searchLecturesByKeyword(
            @RequestParam("keyword") String keyword) {

        List<LectureNameOnlyResponseDTO> lectureNames = lectureQueryService.findLectureNamesByKeyword(keyword);
        return new ResponseEntity<>(lectureNames, HttpStatus.OK);
    }

}
