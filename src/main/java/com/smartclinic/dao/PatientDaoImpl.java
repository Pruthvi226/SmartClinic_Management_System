package com.smartclinic.dao;
import com.smartclinic.model.Patient;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository
public class PatientDaoImpl extends GenericDaoImpl<Patient, Long> implements PatientDao {
    @Override
    public List<Patient> searchByNameOrPhone(String keyword) {
        return getSession().createQuery(
                "from Patient p where lower(p.name) like :kw or lower(p.phone) like :kw or lower(p.email) like :kw order by p.name",
                Patient.class)
                .setParameter("kw", "%" + keyword.toLowerCase() + "%")
                .list();
    }
}
