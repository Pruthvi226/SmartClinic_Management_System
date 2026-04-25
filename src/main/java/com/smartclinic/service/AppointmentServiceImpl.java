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
import java.util.List;

@Service
@Transactional
public class AppointmentServiceImpl implements AppointmentService {

    @Autowired
    private AppointmentDao appointmentDao;

    @Autowired
    private DoctorDao doctorDao;

    @Override
    public void bookAppointment(Appointment appointment) {
        appointmentDao.save(appointment);
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
    public List<LocalDateTime> getAvailableSlots(Long doctorId, LocalDate date, Appointment.Priority priority) {
        Doctor doctor = doctorDao.findById(doctorId);
        if (doctor == null) return java.util.Collections.emptyList();
        
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.plusDays(1).atStartOfDay();
        
        List<Appointment> bookedAppointments = appointmentDao.findByDoctorAndDatePrefix(doctorId, startOfDay, endOfDay);
        
        return SlotAllocator.allocateSlots(doctor, date, priority, bookedAppointments);
    }
}
