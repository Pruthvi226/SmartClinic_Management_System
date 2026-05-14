package com.smartclinic.dao;

import com.smartclinic.model.ReminderLog;
import java.util.List;

public interface ReminderLogDao extends GenericDao<ReminderLog, Long> {
    List<ReminderLog> findAllFetched();
}
