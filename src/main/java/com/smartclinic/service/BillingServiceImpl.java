package com.smartclinic.service;

import com.smartclinic.dao.AppointmentDao;
import com.smartclinic.dao.BillingDao;
import com.smartclinic.model.Appointment;
import com.smartclinic.model.Billing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class BillingServiceImpl implements BillingService {

    @Autowired
    private BillingDao billingDao;

    @Autowired
    private AppointmentDao appointmentDao;

    @Override
    public Billing generateBill(Long appointmentId) {
        Appointment appointment = appointmentDao.findById(appointmentId);
        if (appointment == null) return null;

        Billing billing = new Billing();
        billing.setAppointment(appointment);
        
        // Example base fee
        BigDecimal baseAmount = new BigDecimal("100.00");
        if (appointment.getPriority() == Appointment.Priority.EMERGENCY) {
            baseAmount = new BigDecimal("200.00");
        } else if (appointment.getPriority() == Appointment.Priority.SENIOR) {
            baseAmount = new BigDecimal("80.00"); // Discount
        }

        billing.setAmount(baseAmount);
        
        // Execute stored procedure to calculate tax
        BigDecimal tax = billingDao.calculateTax(baseAmount);
        billing.setTax(tax);
        billing.setTotal(baseAmount.add(tax));
        billing.setPaymentStatus("PENDING");

        billingDao.save(billing);
        return billing;
    }

    @Override
    public List<Billing> findAll() {
        return billingDao.findAll();
    }

    @Override
    public Billing findById(Long id) {
        return billingDao.findById(id);
    }

    @Override
    public void updatePaymentStatus(Long id, String status) {
        Billing billing = billingDao.findById(id);
        if (billing != null) {
            billing.setPaymentStatus(status);
            billingDao.update(billing);
        }
    }
}
