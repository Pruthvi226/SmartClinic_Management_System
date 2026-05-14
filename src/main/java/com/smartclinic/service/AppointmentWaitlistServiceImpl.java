package com.smartclinic.service;

import com.smartclinic.dao.AppointmentWaitlistDao;
import com.smartclinic.dao.DoctorDao;
import com.smartclinic.dao.PatientDao;
import com.smartclinic.model.Appointment;
import com.smartclinic.model.AppointmentWaitlist;
import com.smartclinic.model.Doctor;
import com.smartclinic.model.Patient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class AppointmentWaitlistServiceImpl implements AppointmentWaitlistService {

    @Autowired
    private AppointmentWaitlistDao appointmentWaitlistDao;

    @Autowired
    private PatientDao patientDao;

    @Autowired
    private DoctorDao doctorDao;

    @Override
    public List<AppointmentWaitlist> findAll() {
        return appointmentWaitlistDao.findAllFetched();
    }

    @Override
    public List<AppointmentWaitlist> findByStatus(AppointmentWaitlist.Status status) {
        return status == null ? findAll() : appointmentWaitlistDao.findByStatus(status);
    }

    @Override
    public AppointmentWaitlist addEntry(Long patientId, Long doctorId, LocalDate requestedDate,
            Appointment.Priority priority, String notes) {
        Patient patient = patientDao.findById(patientId);
        Doctor doctor = doctorDao.findById(doctorId);
        if (patient == null || doctor == null) {
            throw new IllegalArgumentException("Patient and doctor are required");
        }

        AppointmentWaitlist entry = new AppointmentWaitlist();
        entry.setPatient(patient);
        entry.setDoctor(doctor);
        entry.setRequestedDate(requestedDate);
        entry.setPriority(priority == null ? Appointment.Priority.NORMAL : priority);
        entry.setStatus(AppointmentWaitlist.Status.WAITING);
        entry.setNotes(notes);
        appointmentWaitlistDao.save(entry);
        return entry;
    }

    @Override
    public AppointmentWaitlist notifyFirstForSlot(Long doctorId, LocalDate requestedDate) {
        List<AppointmentWaitlist> waiting = appointmentWaitlistDao.findByDoctorDateAndStatus(
                doctorId,
                requestedDate,
                AppointmentWaitlist.Status.WAITING);
        if (waiting.isEmpty()) {
            return null;
        }

        AppointmentWaitlist entry = waiting.get(0);
        entry.setStatus(AppointmentWaitlist.Status.NOTIFIED);
        appointmentWaitlistDao.update(entry);
        return entry;
    }

    @Override
    public void updateStatus(Long id, AppointmentWaitlist.Status status) {
        AppointmentWaitlist entry = appointmentWaitlistDao.findById(id);
        if (entry != null) {
            entry.setStatus(status);
            appointmentWaitlistDao.update(entry);
        }
    }
}
