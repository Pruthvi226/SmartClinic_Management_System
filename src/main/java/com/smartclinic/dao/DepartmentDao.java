package com.smartclinic.dao;

import com.smartclinic.model.Department;

public interface DepartmentDao extends GenericDao<Department, Long> {
    Department findByName(String name);
}
