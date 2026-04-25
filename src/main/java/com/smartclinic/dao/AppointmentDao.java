package com.smartclinic.dao;
import com.smartclinic.model.Appointment;
import java.time.LocalDateTime;
import java.util.List;
public interface AppointmentDao extends GenericDao<Appointment, Long> {
    List<Appointment> findUpcomingByDoctor(Long doctorId);
    List<Appointment> findByDoctorAndDatePrefix(Long doctorId, LocalDateTime startOfDay, LocalDateTime endOfDay);
    List<Appointment> findByPatientId(Long patientId);
}
