package com.smartclinic.service;

import com.smartclinic.dao.DepartmentDao;
import com.smartclinic.model.Department;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class DepartmentServiceImpl implements DepartmentService {
    @Autowired
    private DepartmentDao departmentDao;

    @Override
    public List<Department> findAll() {
        return departmentDao.findAll();
    }

    @Override
    public Department findById(Long id) {
        return departmentDao.findById(id);
    }

    @Override
    public void save(Department department) {
        Department existing = department.getId() != null ? departmentDao.findById(department.getId()) : departmentDao.findByName(department.getName());
        Department target = existing == null ? department : existing;
        target.setName(clean(department.getName()));
        target.setDescription(clean(department.getDescription()));
        target.setActive(department.isActive());
        departmentDao.save(target);
    }

    @Override
    public void toggleActive(Long id) {
        Department department = departmentDao.findById(id);
        if (department != null) {
            department.setActive(!department.isActive());
            departmentDao.update(department);
        }
    }

    private String clean(String value) {
        return value == null ? null : value.trim();
    }
}
