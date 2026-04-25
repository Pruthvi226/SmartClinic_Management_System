package com.smartclinic.service;

import com.smartclinic.dao.PatientDao;
import com.smartclinic.model.Patient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PatientServiceImpl implements PatientService {

    @Autowired
    private PatientDao patientDao;

    @Override
    public void registerPatient(Patient patient) {
        patientDao.save(patient);
    }

    @Override
    public Patient findById(Long id) {
        return patientDao.findById(id);
    }

    @Override
    public List<Patient> searchPatients(String keyword) {
        return patientDao.searchByNameOrPhone(keyword);
    }

    @Override
    public List<Patient> findAll() {
        return patientDao.findAll();
    }
}
