package com.smartclinic.service;

import com.smartclinic.dao.AppointmentDao;
import com.smartclinic.dao.ReminderLogDao;
import com.smartclinic.model.Appointment;
import com.smartclinic.model.ReminderLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class ReminderLogServiceImpl implements ReminderLogService {
    @Autowired
    private ReminderLogDao reminderLogDao;

    @Autowired
    private AppointmentDao appointmentDao;

    @Override
    public ReminderLog sendMockReminder(Long appointmentId, String channel) {
        Appointment appointment = appointmentDao.findById(appointmentId);
        if (appointment == null) {
            throw new IllegalArgumentException("Appointment not found");
        }
        String normalizedChannel = channel == null || channel.trim().isEmpty() ? "SMS" : channel.trim().toUpperCase();
        ReminderLog log = new ReminderLog();
        log.setAppointment(appointment);
        log.setChannel(normalizedChannel);
        log.setRecipient("EMAIL".equals(normalizedChannel) ? appointment.getPatient().getEmail() : appointment.getPatient().getPhone());
        log.setStatus("MOCK_SENT");
        log.setMessage("Reminder for " + appointment.getSlotDatetime() + " with Dr. " + appointment.getDoctor().getUser().getName());
        reminderLogDao.save(log);
        return log;
    }

    @Override
    public List<ReminderLog> findAll() {
        return reminderLogDao.findAllFetched();
    }
}
