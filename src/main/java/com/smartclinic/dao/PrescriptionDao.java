package com.smartclinic.dao;
import com.smartclinic.model.Prescription;
import java.util.List;
public interface PrescriptionDao extends GenericDao<Prescription, Long> {
    List<Prescription> findByPatientId(Long patientId);
    List<Prescription> findByDoctorId(Long doctorId);
    Prescription findByAppointmentId(Long appointmentId);
    List<Prescription> findAllFetched();
    List<Prescription> findPendingFetched();
}
