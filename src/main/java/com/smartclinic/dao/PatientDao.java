package com.smartclinic.dao;
import com.smartclinic.model.Patient;
import java.util.List;
public interface PatientDao extends GenericDao<Patient, Long> {
    List<Patient> searchByNameOrPhone(String keyword);
}
