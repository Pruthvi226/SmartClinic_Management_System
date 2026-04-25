package com.smartclinic.dao;
import com.smartclinic.model.Patient;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository
public class PatientDaoImpl extends GenericDaoImpl<Patient, Long> implements PatientDao {
    @Override
    public List<Patient> searchByNameOrPhone(String keyword) {
        return getSession().createQuery("from Patient p where p.name like :kw or p.phone like :kw", Patient.class)
                .setParameter("kw", "%" + keyword + "%")
                .list();
    }
}
