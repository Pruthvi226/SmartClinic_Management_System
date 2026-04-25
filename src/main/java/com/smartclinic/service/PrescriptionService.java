package com.smartclinic.service;
import com.smartclinic.model.Prescription;
import java.util.List;
public interface PrescriptionService {
    void createPrescription(Prescription prescription);
    List<Prescription> getPatientPrescriptions(Long patientId);
    List<Prescription> getAllPrescriptionsFetched();
    Prescription findById(Long id);
}
