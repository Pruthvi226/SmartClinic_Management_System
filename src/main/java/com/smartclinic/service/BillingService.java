package com.smartclinic.service;
import com.smartclinic.model.Billing;
import java.math.BigDecimal;
import java.util.List;
public interface BillingService {
    Billing generateBill(Long appointmentId);
    List<Billing> findAll();
    Billing findById(Long id);
    void updatePaymentStatus(Long id, String status);
    void updatePayment(Long id, String status, String paymentMode, BigDecimal paidAmount, String paymentReference);
    void updatePayment(Long id, String status, String paymentMode, BigDecimal paidAmount, String paymentReference,
            BigDecimal discountAmount, String discountReason, String insuranceProvider, String insuranceClaimNumber,
            String insuranceStatus);
    void recordRefund(Long id, BigDecimal refundAmount, String refundReason);
}
