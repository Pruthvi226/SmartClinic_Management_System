package com.smartclinic.dao;
import com.smartclinic.model.Doctor;
public interface DoctorDao extends GenericDao<Doctor, Long> {
    Doctor findByUserId(Long userId);
}
