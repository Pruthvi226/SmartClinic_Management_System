package com.smartclinic.controller;

import com.smartclinic.model.Billing;
import com.smartclinic.service.BillingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/billing")
public class BillingApiController {

    @Autowired
    private BillingService billingService;

    /**
     * Get all billing records
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllBillings() {
        try {
            List<Billing> billings = billingService.findAll();
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("count", billings.size());
            response.put("data", billings);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return handleException("Failed to fetch billing records", e);
        }
    }

    /**
     * Get billing record by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getBillingById(@PathVariable Long id) {
        try {
            Billing billing = billingService.findById(id);
            if (billing == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(createErrorResponse("Billing record not found with id: " + id));
            }
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("data", billing);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return handleException("Failed to fetch billing record", e);
        }
    }

    /**
     * Generate a new billing record for an appointment
     */
    @PostMapping("/generate")
    public ResponseEntity<Map<String, Object>> generateBilling(@RequestParam Long appointmentId) {
        try {
            Billing billing = billingService.generateBill(appointmentId);
            if (billing == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(createErrorResponse("Could not generate bill for appointment id: " + appointmentId));
            }
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Billing record generated successfully");
            response.put("data", billing);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return handleException("Failed to generate billing record", e);
        }
    }

    /**
     * Update payment status of a billing record
     */
    @PutMapping("/{id}/payment-status")
    public ResponseEntity<Map<String, Object>> updatePaymentStatus(
            @PathVariable Long id,
            @RequestParam String status,
            @RequestParam(value = "paymentMode", required = false) String paymentMode,
            @RequestParam(value = "paidAmount", required = false) BigDecimal paidAmount,
            @RequestParam(value = "paymentReference", required = false) String paymentReference) {
        try {
            // Validate status
            if (!isValidPaymentStatus(status)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(createErrorResponse(
                                "Invalid payment status. Allowed values: PENDING, PARTIAL, PAID, FAILED, REFUNDED"));
            }

            Billing billing = billingService.findById(id);
            if (billing == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(createErrorResponse("Billing record not found with id: " + id));
            }

            billingService.updatePayment(id, status, paymentMode, paidAmount, paymentReference);
            billing = billingService.findById(id); // Reload to get updated data

            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Payment status updated successfully");
            response.put("data", billing);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return handleException("Failed to update payment status", e);
        }
    }

    /**
     * Get billing summary (total amount, pending amount, paid amount)
     */
    @GetMapping("/summary")
    public ResponseEntity<Map<String, Object>> getBillingSummary() {
        try {
            List<Billing> billings = billingService.findAll();

            double totalAmount = 0;
            double paidAmount = 0;
            double pendingAmount = 0;

            for (Billing billing : billings) {
                totalAmount += billing.getTotal() != null ? billing.getTotal().doubleValue() : 0;
                if ("PAID".equals(billing.getPaymentStatus())) {
                    paidAmount += billing.getTotal() != null ? billing.getTotal().doubleValue() : 0;
                } else if ("PENDING".equals(billing.getPaymentStatus())) {
                    pendingAmount += billing.getTotal() != null ? billing.getTotal().doubleValue() : 0;
                }
            }

            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("totalBillingRecords", billings.size());
            response.put("totalAmount", totalAmount);
            response.put("paidAmount", paidAmount);
            response.put("pendingAmount", pendingAmount);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return handleException("Failed to fetch billing summary", e);
        }
    }

    private boolean isValidPaymentStatus(String status) {
        return status != null &&
                (status.equals("PENDING") ||
                        status.equals("PAID") ||
                        status.equals("PARTIAL") ||
                        status.equals("FAILED") ||
                        status.equals("REFUNDED"));
    }

    // Helper method for exception handling
    private ResponseEntity<Map<String, Object>> handleException(String message, Exception e) {
        Map<String, Object> errorResponse = createErrorResponse(message + ": " + e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    private Map<String, Object> createErrorResponse(String message) {
        Map<String, Object> error = new HashMap<>();
        error.put("status", "error");
        error.put("message", message);
        return error;
    }
}
