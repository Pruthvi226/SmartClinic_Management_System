package com.smartclinic.dao;

import com.smartclinic.model.Department;
import org.springframework.stereotype.Repository;

@Repository
public class DepartmentDaoImpl extends GenericDaoImpl<Department, Long> implements DepartmentDao {
    @Override
    public Department findByName(String name) {
        return getSession().createQuery("from Department d where d.name = :name", Department.class)
                .setParameter("name", name)
                .uniqueResult();
    }
}
