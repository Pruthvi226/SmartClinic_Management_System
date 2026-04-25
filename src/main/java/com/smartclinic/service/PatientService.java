package com.smartclinic.service;

import com.smartclinic.model.Patient;
import java.util.List;

public interface PatientService {
    void registerPatient(Patient patient);
    Patient findById(Long id);
    List<Patient> searchPatients(String keyword);
    List<Patient> findAll();
}
