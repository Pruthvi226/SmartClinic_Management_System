package com.smartclinic.service;
import com.smartclinic.model.Prescription;
import java.util.List;
public interface PrescriptionService {
    void createPrescription(Prescription prescription);
    Prescription saveConsultation(Prescription prescription, boolean draft);
    Prescription findByAppointmentId(Long appointmentId);
    List<Prescription> getDoctorPrescriptions(Long doctorId);
    List<Prescription> getPatientPrescriptions(Long patientId);
    List<Prescription> getAllPrescriptionsFetched();
    List<Prescription> getPendingPrescriptionsFetched();
    Prescription findById(Long id);
    void markDispensed(Long id);
}
