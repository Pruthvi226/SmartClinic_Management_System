package com.smartclinic.service;

import com.smartclinic.model.Appointment;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentService {
    void bookAppointment(Appointment appointment);
    List<Appointment> getUpcomingForDoctor(Long doctorId);
    List<Appointment> getPatientHistory(Long patientId);
    List<Appointment> findAll();
    Appointment findById(Long id);
    void updateStatus(Long id, Appointment.Status status);
    void updateQueueState(Long id, String queueState);
    void cancelAppointment(Long id, String reason);
    void rescheduleAppointment(Long id, Long doctorId, LocalDateTime slotDatetime, Appointment.Priority priority, String notes);
    List<LocalDateTime> getAvailableSlots(Long doctorId, LocalDate date, Appointment.Priority priority);
    List<Appointment> getAppointmentsByDate(LocalDate date);
}
