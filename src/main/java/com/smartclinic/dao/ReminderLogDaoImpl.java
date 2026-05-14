package com.smartclinic.dao;

import com.smartclinic.model.ReminderLog;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class ReminderLogDaoImpl extends GenericDaoImpl<ReminderLog, Long> implements ReminderLogDao {
    @Override
    public List<ReminderLog> findAllFetched() {
        return getSession().createQuery(
                "select r from ReminderLog r join fetch r.appointment a join fetch a.patient join fetch a.doctor d join fetch d.user order by r.id desc",
                ReminderLog.class)
                .list();
    }
}
