package com.smartclinic.dao;
import com.smartclinic.model.Doctor;
import org.springframework.stereotype.Repository;
@Repository
public class DoctorDaoImpl extends GenericDaoImpl<Doctor, Long> implements DoctorDao {
    @Override
    public Doctor findByUserId(Long userId) {
        return getSession().createQuery("from Doctor d JOIN FETCH d.user where d.user.id = :userId", Doctor.class)
                .setParameter("userId", userId)
                .uniqueResult();
    }
}
