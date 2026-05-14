package com.smartclinic.dao;

import com.smartclinic.model.AppointmentWaitlist;
import java.time.LocalDate;
import java.util.List;

public interface AppointmentWaitlistDao extends GenericDao<AppointmentWaitlist, Long> {
    List<AppointmentWaitlist> findAllFetched();
    List<AppointmentWaitlist> findByStatus(AppointmentWaitlist.Status status);
    List<AppointmentWaitlist> findByDoctorDateAndStatus(Long doctorId, LocalDate requestedDate, AppointmentWaitlist.Status status);
}
