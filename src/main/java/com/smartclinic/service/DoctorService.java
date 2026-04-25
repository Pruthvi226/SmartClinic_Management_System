package com.smartclinic.service;

import com.smartclinic.model.Doctor;
import java.util.List;

public interface DoctorService {
    List<Doctor> findAll();
    Doctor findById(Long id);
    Doctor findByUserId(Long userId);
    void save(Doctor doctor);
}
