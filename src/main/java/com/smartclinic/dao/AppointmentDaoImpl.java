package com.smartclinic.dao;
import com.smartclinic.model.Appointment;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
@Repository
public class AppointmentDaoImpl extends GenericDaoImpl<Appointment, Long> implements AppointmentDao {
    @Override
    public List<Appointment> findUpcomingByDoctor(Long doctorId) {
        return getSession().createNamedQuery("Appointment.findUpcomingByDoctor", Appointment.class)
                .setParameter("doctorId", doctorId)
                .list();
    }
    @Override
    public List<Appointment> findByDoctorAndDatePrefix(Long doctorId, LocalDateTime startOfDay, LocalDateTime endOfDay) {
        return getSession().createQuery("from Appointment a where a.doctor.id = :doctorId and a.slotDatetime between :start and :end and a.status != 'CANCELLED'", Appointment.class)
                .setParameter("doctorId", doctorId)
                .setParameter("start", startOfDay)
                .setParameter("end", endOfDay)
                .list();
    }
    @Override
    public List<Appointment> findByPatientId(Long patientId) {
        return getSession().createQuery("from Appointment a JOIN FETCH a.doctor d JOIN FETCH d.user u where a.patient.id = :patientId order by a.slotDatetime desc", Appointment.class)
                .setParameter("patientId", patientId)
                .list();
    }
}
