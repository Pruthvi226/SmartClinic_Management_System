package com.smartclinic.service;

import com.smartclinic.model.Department;
import java.util.List;

public interface DepartmentService {
    List<Department> findAll();
    Department findById(Long id);
    void save(Department department);
    void toggleActive(Long id);
}
