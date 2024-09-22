package com.allclearlecture.domain.fileupload.consumer;


import com.allclearlecture.domain.lecture.entity.Lecture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;

public class LecturesDBHandler {
    public static final Logger logger = LoggerFactory.getLogger(LecturesDBHandler.class.getName());
    private Connection connection = null;
    private PreparedStatement insertPrepared = null;

    // Lecture 테이블에 데이터 삽입하기 위한 SQL 쿼리
    private static final String INSERT_LECTURE_SQL = "INSERT INTO public.lecture " +
            "(lecture_id, department_id, lecture_code, lecture_name, division, " +
            " professor_id, credit,allowed_number_of_students, current_number_of_students, grade, lecture_day_and_room,lecture_classification, lecture_time, " +
            " lecture_year, semester, syllabus, del_status,created_date,modified_date) " +
            "VALUES (?, ?, ?, ?, ?, ?,?,?, ?, ?, ?, ?, ?, ?, ?, ?,?,?,?)";

    //파일이 바뀌었을 때 해당 아이디를 가진 강의가 있는지 확인하는 매서드
    public boolean lectureExists(Long lectureId) {
        String checkSql = "SELECT COUNT(*) FROM public.lecture WHERE lecture_id = ?";
        try (PreparedStatement checkPrepared = connection.prepareStatement(checkSql)) {
            checkPrepared.setLong(1, lectureId);
            try (ResultSet rs = checkPrepared.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            logger.error("Error checking if lecture exists: {}", e.getMessage(), e);
        }
        return false;
    }
    public LecturesDBHandler(String url, String user, String password) {
        try {
            this.connection = DriverManager.getConnection(url, user, password);
            this.insertPrepared = this.connection.prepareStatement(INSERT_LECTURE_SQL);
        } catch(SQLException e) {
            logger.error(e.getMessage());
        }
    }
    public void insertLecture(Lecture lecture) {
        try {

            logger.info("Inserting lecture: {}", lecture);
            PreparedStatement pstmt = this.connection.prepareStatement(INSERT_LECTURE_SQL);
            pstmt.setLong(1, lecture.getId()); // 강의 아이디
            pstmt.setLong(2, lecture.getDepartment() != null ? lecture.getDepartment().getId() : Types.NULL); // 학과 아이디
            pstmt.setString(3, lecture.getLectureCode()); // 학수번호
            pstmt.setString(4, lecture.getLectureName()); // 강의 제목
            pstmt.setString(5, lecture.getDivision()); // 분반
            pstmt.setLong(6, lecture.getProfessor() != null ? lecture.getProfessor().getId() : Types.NULL); // 교수 아이디
            pstmt.setInt(7, lecture.getCredit()); // 학점
            pstmt.setInt(8, lecture.getAllowedNumberOfStudents()); // 수강 가능 인원
            pstmt.setInt(9, lecture.getCurrentNumberOfStudents()); // 현재 수강 인원
            pstmt.setString(10, lecture.getGrade()); // 이수필수여부
            pstmt.setString(11, lecture.getLectureDayAndRoom()); // 강의실 및 시간
            pstmt.setString(12, lecture.getLectureClassification()); // 강의실 및 시간
            pstmt.setString(13, lecture.getLectureTime()); // 수업시간
            pstmt.setInt(14, lecture.getLectureYear()); // 수업년도
            pstmt.setInt(15, lecture.getSemester()); // 학기
            pstmt.setString(16, lecture.getSyllabus()); // 강의 계획서
            pstmt.setBoolean(17, lecture.isDelStatus()); // del_status 값 추가
            pstmt.setTimestamp(18, Timestamp.valueOf(lecture.getCreatedDate())); // created_date 값 추가
            pstmt.setTimestamp(19, Timestamp.valueOf(lecture.getModifiedDate())); // modified_date 값 추가

            pstmt.executeUpdate();
        } catch (SQLException e) {
            logger.error("Error inserting lecture: {}", e.getMessage(), e);
        }
    }

    public void insertOrUpdateLecture(Lecture lecture) {
        String updateSql = "UPDATE public.lecture SET department_id = ?, lecture_code = ?, lecture_name = ?, " +
                "division = ?, professor_id = ?, credit = ?,allowed_number_of_students = ?,current_number_of_students = ?  , grade = ?, lecture_day_and_room = ?,lecture_classification = ?, lecture_time = ?, " +
                "lecture_year = ?, semester = ?, syllabus = ?, del_status = ?,modified_date = ? WHERE lecture_id = ?";

        if (lectureExists(lecture.getId())) {
            // Lecture exists, perform an update
            try (PreparedStatement pstmt = connection.prepareStatement(updateSql)) {
                pstmt.setLong(1, lecture.getDepartment() != null ? lecture.getDepartment().getId() : Types.NULL);
                pstmt.setString(2, lecture.getLectureCode());
                pstmt.setString(3, lecture.getLectureName());
                pstmt.setString(4, lecture.getDivision());
                pstmt.setLong(5, lecture.getProfessor() != null ? lecture.getProfessor().getId() : Types.NULL);
                pstmt.setInt(6, lecture.getCredit());
                pstmt.setInt(7, lecture.getAllowedNumberOfStudents()); // 수강 가능 인원
                pstmt.setInt(8, lecture.getCurrentNumberOfStudents()); // 현재 수강 인원
                pstmt.setString(9, lecture.getGrade());
                pstmt.setString(10, lecture.getLectureDayAndRoom());
                pstmt.setString(11, lecture.getLectureClassification());
                pstmt.setString(12, lecture.getLectureTime());
                pstmt.setInt(13, lecture.getLectureYear());
                pstmt.setInt(14, lecture.getSemester());
                pstmt.setString(15, lecture.getSyllabus());
                pstmt.setBoolean(16, lecture.isDelStatus());
                pstmt.setTimestamp(17, Timestamp.valueOf(LocalDateTime.now())); // Update modified date
                pstmt.setLong(18, lecture.getId());

                pstmt.executeUpdate();
            } catch (SQLException e) {
                logger.error("Error updating lecture: {}", e.getMessage(), e);
            }
        } else {
            // Lecture does not exist, perform an insert
            insertLecture(lecture);
        }
    }
    public void close() {
        try {
            logger.info("###### LecturesDBHandler is closing");
            if (this.insertPrepared != null) this.insertPrepared.close();
            if (this.connection != null) this.connection.close();
        } catch(SQLException e) {
            logger.error(e.getMessage());
        }
    }

    public static void main(String[] args) {
    }

}
