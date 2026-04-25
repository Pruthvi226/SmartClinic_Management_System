package com.smartclinic.service;

import com.smartclinic.dao.PrescriptionDao;
import com.smartclinic.model.Prescription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PrescriptionServiceImpl implements PrescriptionService {

    @Autowired
    private PrescriptionDao prescriptionDao;

    @Override
    public void createPrescription(Prescription prescription) {
        prescriptionDao.save(prescription);
    }

    @Override
    public List<Prescription> getPatientPrescriptions(Long patientId) {
        return prescriptionDao.findByPatientId(patientId);
    }

    @Override
    public List<Prescription> getAllPrescriptionsFetched() {
        return prescriptionDao.findAllFetched();
    }

    @Override
    public Prescription findById(Long id) {
        return prescriptionDao.findById(id);
    }
}
