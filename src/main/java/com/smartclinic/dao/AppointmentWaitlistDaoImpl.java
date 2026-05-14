package com.smartclinic.dao;

import com.smartclinic.model.AppointmentWaitlist;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public class AppointmentWaitlistDaoImpl extends GenericDaoImpl<AppointmentWaitlist, Long> implements AppointmentWaitlistDao {
    @Override
    public AppointmentWaitlist findById(Long id) {
        return getSession()
                .createQuery("select w from AppointmentWaitlist w join fetch w.patient join fetch w.doctor d join fetch d.user where w.id = :id", AppointmentWaitlist.class)
                .setParameter("id", id)
                .uniqueResult();
    }

    @Override
    public List<AppointmentWaitlist> findAllFetched() {
        return getSession()
                .createQuery("select w from AppointmentWaitlist w join fetch w.patient join fetch w.doctor d join fetch d.user order by w.createdAt desc", AppointmentWaitlist.class)
                .list();
    }

    @Override
    public List<AppointmentWaitlist> findByStatus(AppointmentWaitlist.Status status) {
        return getSession()
                .createQuery("select w from AppointmentWaitlist w join fetch w.patient join fetch w.doctor d join fetch d.user where w.status = :status order by w.createdAt asc", AppointmentWaitlist.class)
                .setParameter("status", status)
                .list();
    }

    @Override
    public List<AppointmentWaitlist> findByDoctorDateAndStatus(Long doctorId, LocalDate requestedDate, AppointmentWaitlist.Status status) {
        return getSession()
                .createQuery("select w from AppointmentWaitlist w join fetch w.patient join fetch w.doctor d join fetch d.user where w.doctor.id = :doctorId and w.requestedDate = :requestedDate and w.status = :status order by w.createdAt asc", AppointmentWaitlist.class)
                .setParameter("doctorId", doctorId)
                .setParameter("requestedDate", requestedDate)
                .setParameter("status", status)
                .list();
    }
}
