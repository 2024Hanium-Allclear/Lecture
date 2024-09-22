package com.allclearlecture.domain.lecture.repository;


import com.allclearlecture.domain.lecture.entity.Professor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfessorRepository extends JpaRepository<Professor, Long> {
}
