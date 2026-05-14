package com.smartclinic.service;

import com.smartclinic.model.Appointment;
import com.smartclinic.model.AppointmentWaitlist;
import java.time.LocalDate;
import java.util.List;

public interface AppointmentWaitlistService {
    List<AppointmentWaitlist> findAll();
    List<AppointmentWaitlist> findByStatus(AppointmentWaitlist.Status status);
    AppointmentWaitlist addEntry(Long patientId, Long doctorId, LocalDate requestedDate, Appointment.Priority priority, String notes);
    AppointmentWaitlist notifyFirstForSlot(Long doctorId, LocalDate requestedDate);
    void updateStatus(Long id, AppointmentWaitlist.Status status);
}
