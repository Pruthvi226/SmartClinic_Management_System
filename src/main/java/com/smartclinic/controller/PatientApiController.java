package com.smartclinic.controller;

import com.smartclinic.model.Patient;
import com.smartclinic.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/patients")
public class PatientApiController {

    @Autowired
    private PatientService patientService;

    /**
     * Get all patients
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllPatients() {
        try {
            List<Patient> patients = patientService.findAll();
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("count", patients.size());
            response.put("data", patients);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return handleException("Failed to fetch patients", e);
        }
    }

    /**
     * Get patient by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getPatientById(@PathVariable Long id) {
        try {
            Patient patient = patientService.findById(id);
            if (patient == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(createErrorResponse("Patient not found with id: " + id));
            }
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("data", patient);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return handleException("Failed to fetch patient", e);
        }
    }

    /**
     * Search patients by keyword (name or phone)
     */
    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> searchPatients(@RequestParam String keyword) {
        try {
            List<Patient> patients = patientService.searchPatients(keyword);
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("count", patients.size());
            response.put("data", patients);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return handleException("Failed to search patients", e);
        }
    }

    /**
     * Register a new patient
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> registerPatient(@Valid @RequestBody Patient patient) {
        try {
            if (patient.getName() == null || patient.getName().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(createErrorResponse("Patient name is required"));
            }
            patientService.registerPatient(patient);
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Patient registered successfully");
            response.put("data", patient);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return handleException("Failed to register patient", e);
        }
    }

    /**
     * Update patient information
     */
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updatePatient(
            @PathVariable Long id,
            @Valid @RequestBody Patient patientDetails) {
        try {
            Patient patient = patientService.findById(id);
            if (patient == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(createErrorResponse("Patient not found with id: " + id));
            }

            // Update fields
            if (patientDetails.getName() != null)
                patient.setName(patientDetails.getName());
            if (patientDetails.getDob() != null)
                patient.setDob(patientDetails.getDob());
            if (patientDetails.getGender() != null)
                patient.setGender(patientDetails.getGender());
            if (patientDetails.getPhone() != null)
                patient.setPhone(patientDetails.getPhone());
            if (patientDetails.getEmail() != null)
                patient.setEmail(patientDetails.getEmail());
            if (patientDetails.getBloodGroup() != null)
                patient.setBloodGroup(patientDetails.getBloodGroup());
            if (patientDetails.getAddress() != null)
                patient.setAddress(patientDetails.getAddress());

            patientService.registerPatient(patient);
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Patient updated successfully");
            response.put("data", patient);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return handleException("Failed to update patient", e);
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
