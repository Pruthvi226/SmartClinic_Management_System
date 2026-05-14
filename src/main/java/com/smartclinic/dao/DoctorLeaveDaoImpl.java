package com.smartclinic.dao;

import com.smartclinic.model.DoctorLeave;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public class DoctorLeaveDaoImpl extends GenericDaoImpl<DoctorLeave, Long> implements DoctorLeaveDao {
    @Override
    public List<DoctorLeave> findByDoctorId(Long doctorId) {
        return getSession()
                .createQuery("select l from DoctorLeave l join fetch l.doctor d join fetch d.user where d.id = :doctorId order by l.leaveDate desc", DoctorLeave.class)
                .setParameter("doctorId", doctorId)
                .list();
    }

    @Override
    public DoctorLeave findByDoctorAndDate(Long doctorId, LocalDate leaveDate) {
        return getSession()
                .createQuery("select l from DoctorLeave l join fetch l.doctor d join fetch d.user where d.id = :doctorId and l.leaveDate = :leaveDate", DoctorLeave.class)
                .setParameter("doctorId", doctorId)
                .setParameter("leaveDate", leaveDate)
                .uniqueResult();
    }
}
