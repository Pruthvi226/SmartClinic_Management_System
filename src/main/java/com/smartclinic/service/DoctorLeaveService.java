package com.smartclinic.service;

import com.smartclinic.model.DoctorLeave;
import java.time.LocalDate;
import java.util.List;

public interface DoctorLeaveService {
    List<DoctorLeave> findByDoctorId(Long doctorId);
    boolean isDoctorOnLeave(Long doctorId, LocalDate date);
    void addLeave(Long doctorId, LocalDate leaveDate, String reason);
    void deleteLeave(Long leaveId);
}
