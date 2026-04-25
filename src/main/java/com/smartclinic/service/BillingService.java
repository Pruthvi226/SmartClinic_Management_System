package com.smartclinic.service;
import com.smartclinic.model.Billing;
import java.util.List;
public interface BillingService {
    Billing generateBill(Long appointmentId);
    List<Billing> findAll();
    Billing findById(Long id);
    void updatePaymentStatus(Long id, String status);
}
