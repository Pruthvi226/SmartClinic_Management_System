package com.smartclinic.dao;
import com.smartclinic.model.Prescription;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository
public class PrescriptionDaoImpl extends GenericDaoImpl<Prescription, Long> implements PrescriptionDao {
    @Override
    public List<Prescription> findByPatientId(Long patientId) {
        return getSession().createQuery("select distinct p from Prescription p JOIN FETCH p.doctor d JOIN FETCH d.user u LEFT JOIN FETCH p.items where p.patient.id = :patientId order by p.id desc", Prescription.class)
                .setParameter("patientId", patientId)
                .list();
    }
    @Override
    public List<Prescription> findAllFetched() {
        return getSession().createQuery("select distinct p from Prescription p JOIN FETCH p.patient JOIN FETCH p.doctor d JOIN FETCH d.user LEFT JOIN FETCH p.items order by p.id desc", Prescription.class).list();
    }
}
