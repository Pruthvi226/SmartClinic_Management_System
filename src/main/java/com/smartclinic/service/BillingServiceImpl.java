package com.smartclinic.service;

import com.smartclinic.dao.AppointmentDao;
import com.smartclinic.dao.BillingDao;
import com.smartclinic.model.Appointment;
import com.smartclinic.model.Billing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@Transactional
public class BillingServiceImpl implements BillingService {

    @Autowired
    private BillingDao billingDao;

    @Autowired
    private AppointmentDao appointmentDao;

    @Autowired
    private SystemSettingService systemSettingService;

    @Override
    public Billing generateBill(Long appointmentId) {
        Appointment appointment = appointmentDao.findById(appointmentId);
        if (appointment == null) return null;

        Billing existing = billingDao.findByAppointmentId(appointmentId);
        if (existing != null) return existing;

        Billing billing = new Billing();
        billing.setAppointment(appointment);
        
        BigDecimal baseAmount = systemSettingService.getDecimal("consultation.fee.normal", new BigDecimal("100.00"));
        if (appointment.getPriority() == Appointment.Priority.EMERGENCY) {
            baseAmount = systemSettingService.getDecimal("consultation.fee.emergency", new BigDecimal("200.00"));
        } else if (appointment.getPriority() == Appointment.Priority.SENIOR) {
            baseAmount = systemSettingService.getDecimal("consultation.fee.senior", new BigDecimal("80.00"));
        }

        billing.setAmount(baseAmount);
        
        BigDecimal tax;
        try {
            tax = billingDao.calculateTax(baseAmount);
        } catch (RuntimeException ex) {
            tax = baseAmount.multiply(new BigDecimal("0.18")).setScale(2, RoundingMode.HALF_UP);
        }
        if (tax == null) {
            tax = baseAmount.multiply(systemSettingService.getDecimal("tax.rate", new BigDecimal("0.18"))).setScale(2, RoundingMode.HALF_UP);
        }
        billing.setTax(tax);
        billing.setTotal(baseAmount.add(tax));
        billing.setPaymentStatus("PENDING");
        billing.setPaymentMode("UNPAID");
        billing.setPaidAmount(BigDecimal.ZERO);
        billing.setReceiptNumber("RCT-" + System.currentTimeMillis());

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
        updatePayment(id, status, null, null, null);
    }

    @Override
    public void updatePayment(Long id, String status, String paymentMode, BigDecimal paidAmount, String paymentReference) {
        updatePayment(id, status, paymentMode, paidAmount, paymentReference, null, null, null, null, null);
    }

    @Override
    public void updatePayment(Long id, String status, String paymentMode, BigDecimal paidAmount, String paymentReference,
            BigDecimal discountAmount, String discountReason, String insuranceProvider, String insuranceClaimNumber,
            String insuranceStatus) {
        Billing billing = billingDao.findById(id);
        if (billing != null) {
            billing.setPaymentStatus(status);
            if (paymentMode != null && !paymentMode.trim().isEmpty()) {
                billing.setPaymentMode(paymentMode.trim());
            }
            if (discountAmount != null) {
                billing.setDiscountAmount(discountAmount.max(BigDecimal.ZERO));
            }
            if (discountReason != null) {
                billing.setDiscountReason(discountReason.trim());
            }
            if (paidAmount != null) {
                billing.setPaidAmount(paidAmount);
            } else if ("PAID".equals(status)) {
                billing.setPaidAmount(billing.getPayableTotal());
            } else if ("PENDING".equals(status)) {
                billing.setPaidAmount(BigDecimal.ZERO);
            }
            if (paymentReference != null) {
                billing.setPaymentReference(paymentReference.trim());
            }
            if (insuranceProvider != null) {
                billing.setInsuranceProvider(insuranceProvider.trim());
            }
            if (insuranceClaimNumber != null) {
                billing.setInsuranceClaimNumber(insuranceClaimNumber.trim());
            }
            if (insuranceStatus != null && !insuranceStatus.trim().isEmpty()) {
                billing.setInsuranceStatus(insuranceStatus.trim());
            }
            billingDao.update(billing);
        }
    }

    @Override
    public void recordRefund(Long id, BigDecimal refundAmount, String refundReason) {
        Billing billing = billingDao.findById(id);
        if (billing == null) {
            throw new IllegalArgumentException("Bill not found");
        }
        billing.setRefundAmount(refundAmount == null ? BigDecimal.ZERO : refundAmount.max(BigDecimal.ZERO));
        billing.setRefundReason(refundReason == null ? null : refundReason.trim());
        billing.setPaymentStatus("REFUNDED");
        billingDao.update(billing);
    }
}
