package com.smartclinic.service;

import com.smartclinic.model.Appointment;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentService {
    void bookAppointment(Appointment appointment);
    List<Appointment> getUpcomingForDoctor(Long doctorId);
    List<Appointment> getPatientHistory(Long patientId);
    Appointment findById(Long id);
    void updateStatus(Long id, Appointment.Status status);
    List<LocalDateTime> getAvailableSlots(Long doctorId, LocalDate date, Appointment.Priority priority);
}
