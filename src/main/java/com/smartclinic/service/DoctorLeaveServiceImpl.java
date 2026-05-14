package com.smartclinic.service;

import com.smartclinic.dao.DoctorDao;
import com.smartclinic.dao.DoctorLeaveDao;
import com.smartclinic.model.Doctor;
import com.smartclinic.model.DoctorLeave;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class DoctorLeaveServiceImpl implements DoctorLeaveService {

    @Autowired
    private DoctorLeaveDao doctorLeaveDao;

    @Autowired
    private DoctorDao doctorDao;

    @Override
    public List<DoctorLeave> findByDoctorId(Long doctorId) {
        return doctorLeaveDao.findByDoctorId(doctorId);
    }

    @Override
    public boolean isDoctorOnLeave(Long doctorId, LocalDate date) {
        return doctorLeaveDao.findByDoctorAndDate(doctorId, date) != null;
    }

    @Override
    public void addLeave(Long doctorId, LocalDate leaveDate, String reason) {
        if (doctorLeaveDao.findByDoctorAndDate(doctorId, leaveDate) != null) {
            return;
        }

        Doctor doctor = doctorDao.findById(doctorId);
        if (doctor == null) {
            throw new IllegalArgumentException("Doctor not found");
        }

        DoctorLeave leave = new DoctorLeave();
        leave.setDoctor(doctor);
        leave.setLeaveDate(leaveDate);
        leave.setReason(reason);
        doctorLeaveDao.save(leave);
    }

    @Override
    public void deleteLeave(Long leaveId) {
        DoctorLeave leave = doctorLeaveDao.findById(leaveId);
        if (leave != null) {
            doctorLeaveDao.delete(leave);
        }
    }
}
