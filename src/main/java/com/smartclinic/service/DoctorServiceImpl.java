package com.smartclinic.service;

import com.smartclinic.dao.DoctorDao;
import com.smartclinic.model.Doctor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class DoctorServiceImpl implements DoctorService {

    @Autowired
    private DoctorDao doctorDao;

    @Override
    public List<Doctor> findAll() {
        return doctorDao.findAll();
    }

    @Override
    public Doctor findById(Long id) {
        return doctorDao.findById(id);
    }

    @Override
    public Doctor findByUserId(Long userId) {
        return doctorDao.findByUserId(userId);
    }

    @Override
    public void save(Doctor doctor) {
        doctorDao.save(doctor);
    }
}
