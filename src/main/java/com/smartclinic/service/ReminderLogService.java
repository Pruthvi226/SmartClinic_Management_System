package com.smartclinic.service;

import com.smartclinic.model.ReminderLog;
import java.util.List;

public interface ReminderLogService {
    ReminderLog sendMockReminder(Long appointmentId, String channel);
    List<ReminderLog> findAll();
}
