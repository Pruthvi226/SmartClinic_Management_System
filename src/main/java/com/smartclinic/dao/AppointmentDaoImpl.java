package com.smartclinic.dao;
import com.smartclinic.model.Appointment;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
@Repository
public class AppointmentDaoImpl extends GenericDaoImpl<Appointment, Long> implements AppointmentDao {
    @Override
    public Appointment findById(Long id) {
        return getSession().createQuery(
                "select a from Appointment a join fetch a.patient join fetch a.doctor d join fetch d.user where a.id = :id",
                Appointment.class)
                .setParameter("id", id)
                .uniqueResult();
    }

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

    @Override
    public List<Appointment> findByDateRange(LocalDateTime startOfDay, LocalDateTime endOfDay) {
        return getSession().createQuery(
                "select a from Appointment a join fetch a.patient join fetch a.doctor d join fetch d.user where a.slotDatetime >= :start and a.slotDatetime < :end order by a.slotDatetime asc",
                Appointment.class)
                .setParameter("start", startOfDay)
                .setParameter("end", endOfDay)
                .list();
    }

    @Override
    public List<Appointment> findAllFetched() {
        return getSession().createQuery(
                "select a from Appointment a join fetch a.patient join fetch a.doctor d join fetch d.user order by a.slotDatetime desc",
                Appointment.class)
                .list();
    }
}
