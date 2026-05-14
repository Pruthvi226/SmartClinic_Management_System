package com.smartclinic.dao;

import com.smartclinic.model.DoctorLeave;
import java.time.LocalDate;
import java.util.List;

public interface DoctorLeaveDao extends GenericDao<DoctorLeave, Long> {
    List<DoctorLeave> findByDoctorId(Long doctorId);
    DoctorLeave findByDoctorAndDate(Long doctorId, LocalDate leaveDate);
}
