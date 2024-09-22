package com.allclearlecture.domain.wishlist.repository;

import com.allclearlecture.domain.wishlist.entity.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface WishlistRepository extends JpaRepository<Wishlist, Long> {
    List<Wishlist> findByStudentId(Long studentId);

    List<Wishlist> findByStudentId(Long studentId, Sort sort);

    void deleteById(Long id); // 삭제 메서드

    @Query("SELECT w FROM Wishlist w WHERE w.lecture.lectureCode = :lectureCode AND w.lecture.division = :division")
    Optional<Wishlist> findByLectureCodeAndDivision(@Param("lectureCode") String lectureCode, @Param("division") String division);
}