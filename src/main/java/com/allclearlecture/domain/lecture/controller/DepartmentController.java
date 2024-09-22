package com.allclearlecture.domain.lecture.controller;



import com.allclearlecture.domain.lecture.dto.DepartmentResponseDTO;
import com.allclearlecture.domain.lecture.dto.LectureNameOnlyResponseDTO;
import com.allclearlecture.domain.lecture.service.DepartmentQueryService;
import com.allclearlecture.domain.lecture.service.LectureQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/departments")
@CrossOrigin(origins = "*")
public class DepartmentController {

    private final DepartmentQueryService departmentQueryService;

    private final LectureQueryService lectureQueryService;

    @Autowired
    public DepartmentController(DepartmentQueryService departmentQueryService, LectureQueryService lectureQueryService) {
        this.departmentQueryService = departmentQueryService;
        this.lectureQueryService = lectureQueryService;
    }


    //전체 학과 조회 - 옵션용
    @GetMapping
    public ResponseEntity<List<DepartmentResponseDTO>> getAllDepartments() {
        List<DepartmentResponseDTO> departments = departmentQueryService.getAllDepartments();
        return new ResponseEntity<>(departments, HttpStatus.OK);
    }

    // 학과 ID에 해당하는 개설 과목 조회 - 제목만
    @GetMapping("/{departmentId}/lectures")
    public ResponseEntity<List<LectureNameOnlyResponseDTO>> findDistinctLectureNamesByDepartmentId(
            @PathVariable Long departmentId,
            @RequestParam(value = "grade", required = false) String grade) {

        List<LectureNameOnlyResponseDTO> lectures = lectureQueryService.findDistinctLectureNamesByDepartmentId(departmentId, grade);
        return new ResponseEntity<>(lectures, HttpStatus.OK);
    }
    // 전체 과목 조회
    @GetMapping("/all")
    public ResponseEntity<List<LectureNameOnlyResponseDTO>> findAllDistinctLectureNamesOrderedByIdAsc() {
        List<LectureNameOnlyResponseDTO> lectures = lectureQueryService.findAllDistinctLectureNamesOrderedByIdAsc();
        return new ResponseEntity<>(lectures, HttpStatus.OK);
    }

}
