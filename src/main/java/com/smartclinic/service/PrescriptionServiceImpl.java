package com.smartclinic.service;

import com.smartclinic.dao.PrescriptionDao;
import com.smartclinic.model.Prescription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.sql.Timestamp;
import java.util.List;

@Service
@Transactional
public class PrescriptionServiceImpl implements PrescriptionService {

    @Autowired
    private PrescriptionDao prescriptionDao;

    @Override
    public void createPrescription(Prescription prescription) {
        prescription.setId(null);
        if (prescription.getItems() != null) {
            prescription.getItems().forEach(item -> {
                item.setId(null);
                item.setPrescription(prescription);
            });
        }
        prescriptionDao.save(prescription);
    }

    @Override
    public Prescription saveConsultation(Prescription prescription, boolean draft) {
        if (prescription.getDiagnosis() == null || prescription.getDiagnosis().trim().isEmpty()) {
            prescription.setDiagnosis("Draft consultation notes pending");
        }
        prescription.setDraft(draft);

        Prescription existing = prescription.getAppointment() == null || prescription.getAppointment().getId() == null
                ? null
                : prescriptionDao.findByAppointmentId(prescription.getAppointment().getId());
        if (existing == null) {
            createPrescription(prescription);
            return prescription;
        }

        existing.setDiagnosis(prescription.getDiagnosis());
        existing.setClinicalTemplate(prescription.getClinicalTemplate());
        existing.setDiagnosisTag(prescription.getDiagnosisTag());
        existing.setBloodPressure(prescription.getBloodPressure());
        existing.setPulse(prescription.getPulse());
        existing.setTemperature(prescription.getTemperature());
        existing.setSpo2(prescription.getSpo2());
        existing.setWeightKg(prescription.getWeightKg());
        existing.setLabOrders(prescription.getLabOrders());
        existing.setFollowUpDays(prescription.getFollowUpDays());
        existing.setRiskFlags(prescription.getRiskFlags());
        existing.setFavoriteName(prescription.getFavoriteName());
        existing.setDraft(draft);
        existing.setDoctor(prescription.getDoctor());
        existing.setPatient(prescription.getPatient());

        if (existing.getItems() == null) {
            existing.setItems(new ArrayList<>());
        } else {
            existing.getItems().clear();
        }
        if (prescription.getItems() != null) {
            prescription.getItems().forEach(item -> {
                item.setId(null);
                item.setPrescription(existing);
                existing.getItems().add(item);
            });
        }

        prescriptionDao.update(existing);
        return existing;
    }

    @Override
    public Prescription findByAppointmentId(Long appointmentId) {
        return prescriptionDao.findByAppointmentId(appointmentId);
    }

    @Override
    public List<Prescription> getDoctorPrescriptions(Long doctorId) {
        return prescriptionDao.findByDoctorId(doctorId);
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
    public List<Prescription> getPendingPrescriptionsFetched() {
        return prescriptionDao.findPendingFetched();
    }

    @Override
    public Prescription findById(Long id) {
        return prescriptionDao.findById(id);
    }

    @Override
    public void markDispensed(Long id) {
        Prescription prescription = prescriptionDao.findById(id);
        if (prescription == null) {
            throw new IllegalArgumentException("Prescription not found");
        }
        prescription.setDispensed(true);
        prescription.setDispensedAt(new Timestamp(System.currentTimeMillis()));
        prescriptionDao.update(prescription);
    }
}
