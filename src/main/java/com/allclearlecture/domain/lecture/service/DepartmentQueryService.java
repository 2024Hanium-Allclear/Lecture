package com.allclearlecture.domain.lecture.service;

import com.allclearlecture.domain.lecture.repository.DepartmentRepository;
import com.allclearlecture.domain.lecture.dto.DepartmentResponseDTO;
import com.allclearlecture.domain.lecture.entity.Department;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Component
public class DepartmentQueryService {

    private final DepartmentRepository departmentRepository;

    @Autowired
    public DepartmentQueryService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    //전체 학과 조회
    public List<DepartmentResponseDTO> getAllDepartments() {
        List<Department> departments = departmentRepository.findAllByOrderByIdAsc();

        return departments.stream()
                .map(DepartmentResponseDTO::fromEntity) // Use the static conversion method from DTO
                .collect(Collectors.toList());
    }
}
