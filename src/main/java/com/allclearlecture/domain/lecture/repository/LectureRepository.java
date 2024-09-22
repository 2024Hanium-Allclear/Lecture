package com.allclearlecture.domain.lecture.repository;
import com.allclearlecture.domain.lecture.entity.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface LectureRepository extends JpaRepository<Lecture,Long> {

    // 학과별 개설 과목 조회
//    @Query("SELECT DISTINCT new com.allclearlecture.lecture.dto.LectureNameOnlyResponseDTO(l.lectureName) FROM Lecture l WHERE l.department.id = :departmentId")
//    List<LectureNameOnlyResponseDTO> findDistinctLectureNamesByDepartmentId(Long departmentId);
    @Query(value = "SELECT DISTINCT lecture_name " +
            "FROM (SELECT lecture_name, lecture_id " +
            "FROM public.lecture " +
            "WHERE department_id = :departmentId " +
            "AND grade = :grade " +  // 학년 조건 추가
            "ORDER BY lecture_id) l",
            nativeQuery = true)
    List<String> findDistinctLectureNamesByDepartmentIdAndGrade(Long departmentId, String grade);

    // 전체 과목 조회
    @Query(value = "SELECT DISTINCT l.lecture_name " +
            "FROM (SELECT DISTINCT lecture_name, lecture_id " +
            "FROM public.lecture " +
            "ORDER BY lecture_id) l", nativeQuery = true)
    List<String> findAllDistinctLectureNamesOrderedByIdAsc();


    // 학과 이름, 학년, 과목 이름에 맞는 과목 조회
    @Query("SELECT l FROM Lecture l JOIN l.department d WHERE d.name LIKE %:departmentName% AND (:grade IS NULL OR l.grade = :grade) AND (:lectureName IS NULL OR l.lectureName LIKE %:lectureName%)")
    List<Lecture> findByDepartmentNameAndGradeAndLectureName(String departmentName, String grade, String lectureName);

    // 키워드에 맞는 과목 조회
    @Query("SELECT l FROM Lecture l WHERE l.lectureName LIKE %:keyword%")
    List<Lecture> findByLectureNameContaining(String keyword);

    // 학수번호와 분반에 맞는 과목 조회
    @Query("SELECT l FROM Lecture l WHERE l.lectureCode = :lectureCode AND (:division IS NULL OR l.division = :division)")
    List<Lecture> findByLectureCodeAndDivision(String lectureCode, String division);

    // 검색어에 맞는 강의 이름 조회
    @Query("SELECT DISTINCT l.lectureName FROM Lecture l WHERE l.lectureName LIKE %:keyword%")
    List<String> findLectureNamesByKeyword(@Param("keyword") String keyword);

}
