package com.smartclinic.controller;

import com.smartclinic.model.Prescription;
import com.smartclinic.service.PrescriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/prescriptions")
public class PrescriptionApiController {

    @Autowired
    private PrescriptionService prescriptionService;

    /**
     * Get all prescriptions
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllPrescriptions() {
        try {
            List<Prescription> prescriptions = prescriptionService.getAllPrescriptionsFetched();
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("count", prescriptions.size());
            response.put("data", prescriptions);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return handleException("Failed to fetch prescriptions", e);
        }
    }

    /**
     * Get prescription by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getPrescriptionById(@PathVariable Long id) {
        try {
            Prescription prescription = prescriptionService.findById(id);
            if (prescription == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(createErrorResponse("Prescription not found with id: " + id));
            }
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("data", prescription);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return handleException("Failed to fetch prescription", e);
        }
    }

    /**
     * Get all prescriptions for a specific patient
     */
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<Map<String, Object>> getPatientPrescriptions(@PathVariable Long patientId) {
        try {
            List<Prescription> prescriptions = prescriptionService.getPatientPrescriptions(patientId);
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("count", prescriptions.size());
            response.put("data", prescriptions);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return handleException("Failed to fetch patient prescriptions", e);
        }
    }

    /**
     * Create a new prescription
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> createPrescription(@Valid @RequestBody Prescription prescription) {
        try {
            if (prescription.getPatient() == null || prescription.getPatient().getId() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(createErrorResponse("Patient ID is required"));
            }
            if (prescription.getDoctor() == null || prescription.getDoctor().getId() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(createErrorResponse("Doctor ID is required"));
            }
            if (prescription.getDiagnosis() == null || prescription.getDiagnosis().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(createErrorResponse("Diagnosis is required"));
            }

            prescriptionService.createPrescription(prescription);
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Prescription created successfully");
            response.put("data", prescription);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return handleException("Failed to create prescription", e);
        }
    }

    /**
     * Get prescription summary (total prescriptions, issued today, etc.)
     */
    @GetMapping("/summary")
    public ResponseEntity<Map<String, Object>> getPrescriptionSummary() {
        try {
            List<Prescription> allPrescriptions = prescriptionService.getAllPrescriptionsFetched();

            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("totalPrescriptions", allPrescriptions.size());
            response.put("data", allPrescriptions);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return handleException("Failed to fetch prescription summary", e);
        }
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
