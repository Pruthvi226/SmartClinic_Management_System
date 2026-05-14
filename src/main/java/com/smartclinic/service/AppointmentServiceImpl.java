package com.smartclinic.service;

import com.smartclinic.dao.AppointmentDao;
import com.smartclinic.dao.DoctorDao;
import com.smartclinic.model.Appointment;
import com.smartclinic.model.Doctor;
import com.smartclinic.util.SlotAllocator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
@Transactional
public class AppointmentServiceImpl implements AppointmentService {

    @Autowired
    private AppointmentDao appointmentDao;

    @Autowired
    private DoctorDao doctorDao;

    @Autowired
    private DoctorLeaveService doctorLeaveService;

    @Override
    public void bookAppointment(Appointment appointment) {
        appointmentDao.save(appointment);
        if (appointment.getTokenNumber() == null || appointment.getTokenNumber().trim().isEmpty()) {
            appointment.setTokenNumber("Q" + appointment.getId());
        }
        if (appointment.getQueueState() == null || appointment.getQueueState().trim().isEmpty()) {
            appointment.setQueueState("WAITING");
        }
    }

    @Override
    public List<Appointment> getUpcomingForDoctor(Long doctorId) {
        return appointmentDao.findUpcomingByDoctor(doctorId);
    }

    @Override
    public List<Appointment> getPatientHistory(Long patientId) {
        return appointmentDao.findByPatientId(patientId);
    }

    @Override
    public List<Appointment> findAll() {
        return appointmentDao.findAllFetched();
    }

    @Override
    public Appointment findById(Long id) {
        return appointmentDao.findById(id);
    }

    @Override
    public void updateStatus(Long id, Appointment.Status status) {
        Appointment appointment = appointmentDao.findById(id);
        if (appointment != null) {
            appointment.setStatus(status);
            appointmentDao.update(appointment);
        }
    }

    @Override
    public void updateQueueState(Long id, String queueState) {
        Appointment appointment = appointmentDao.findById(id);
        if (appointment == null) {
            throw new IllegalArgumentException("Appointment not found");
        }
        appointment.setQueueState(queueState);
        appointmentDao.update(appointment);
    }

    @Override
    public void cancelAppointment(Long id, String reason) {
        Appointment appointment = appointmentDao.findById(id);
        if (appointment == null) {
            throw new IllegalArgumentException("Appointment not found");
        }
        appointment.setStatus(Appointment.Status.CANCELLED);
        if (reason != null && !reason.trim().isEmpty()) {
            appointment.setNotes(appendNote(appointment.getNotes(), "Cancellation reason: " + reason.trim()));
        }
        appointmentDao.update(appointment);
    }

    @Override
    public void rescheduleAppointment(Long id, Long doctorId, LocalDateTime slotDatetime,
            Appointment.Priority priority, String notes) {
        Appointment appointment = appointmentDao.findById(id);
        if (appointment == null) {
            throw new IllegalArgumentException("Appointment not found");
        }

        Doctor doctor = doctorDao.findById(doctorId);
        if (doctor == null) {
            throw new IllegalArgumentException("Doctor not found");
        }

        appointment.setDoctor(doctor);
        appointment.setSlotDatetime(slotDatetime);
        appointment.setPriority(priority);
        appointment.setStatus(Appointment.Status.SCHEDULED);
        appointment.setQueueState("WAITING");
        appointment.setNotes(appendNote(notes, "Rescheduled from appointment #" + id));
        appointmentDao.update(appointment);
    }

    @Override
    public List<LocalDateTime> getAvailableSlots(Long doctorId, LocalDate date, Appointment.Priority priority) {
        Doctor doctor = doctorDao.findById(doctorId);
        if (doctor == null) return Collections.emptyList();
        if (doctorLeaveService.isDoctorOnLeave(doctorId, date)) return Collections.emptyList();
        
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.plusDays(1).atStartOfDay();
        
        List<Appointment> bookedAppointments = appointmentDao.findByDoctorAndDatePrefix(doctorId, startOfDay, endOfDay);
        
        return SlotAllocator.allocateSlots(doctor, date, priority, bookedAppointments);
    }

    @Override
    public List<Appointment> getAppointmentsByDate(LocalDate date) {
        return appointmentDao.findByDateRange(date.atStartOfDay(), date.plusDays(1).atStartOfDay());
    }

    private String appendNote(String currentNotes, String newNote) {
        if (currentNotes == null || currentNotes.trim().isEmpty()) {
            return newNote;
        }
        return currentNotes.trim() + "\n" + newNote;
    }
}
