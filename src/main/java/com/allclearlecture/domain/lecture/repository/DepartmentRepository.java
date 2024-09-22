package com.allclearlecture.domain.lecture.repository;



import com.allclearlecture.domain.lecture.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
    List<Department> findAllByOrderByIdAsc();
}
