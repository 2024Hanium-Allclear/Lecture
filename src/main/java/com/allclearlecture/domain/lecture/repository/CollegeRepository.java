package com.allclearlecture.domain.lecture.repository;


import com.allclearlecture.domain.lecture.entity.College;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CollegeRepository extends JpaRepository<College, Long> {
}

