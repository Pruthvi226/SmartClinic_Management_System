package com.smartclinic.controller;

import com.smartclinic.model.Doctor;
import com.smartclinic.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/doctors")
public class DoctorApiController {

    @Autowired
    private DoctorService doctorService;

    /**
     * Get all doctors
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllDoctors() {
        try {
            List<Doctor> doctors = doctorService.findAll();
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("count", doctors.size());
            response.put("data", doctors);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return handleException("Failed to fetch doctors", e);
        }
    }

    /**
     * Get doctor by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getDoctorById(@PathVariable Long id) {
        try {
            Doctor doctor = doctorService.findById(id);
            if (doctor == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(createErrorResponse("Doctor not found with id: " + id));
            }
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("data", doctor);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return handleException("Failed to fetch doctor", e);
        }
    }

    /**
     * Get doctor by user ID
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<Map<String, Object>> getDoctorByUserId(@PathVariable Long userId) {
        try {
            Doctor doctor = doctorService.findByUserId(userId);
            if (doctor == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(createErrorResponse("Doctor not found for user id: " + userId));
            }
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("data", doctor);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return handleException("Failed to fetch doctor by user id", e);
        }
    }

    /**
     * Create a new doctor record
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> createDoctor(@Valid @RequestBody Doctor doctor) {
        try {
            if (doctor.getUser() == null || doctor.getUser().getId() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(createErrorResponse("User ID is required to create a doctor record"));
            }
            if (doctor.getSpecialization() == null || doctor.getSpecialization().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(createErrorResponse("Specialization is required"));
            }

            doctorService.save(doctor);
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Doctor created successfully");
            response.put("data", doctor);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return handleException("Failed to create doctor", e);
        }
    }

    /**
     * Update doctor information
     */
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateDoctor(
            @PathVariable Long id,
            @Valid @RequestBody Doctor doctorDetails) {
        try {
            Doctor doctor = doctorService.findById(id);
            if (doctor == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(createErrorResponse("Doctor not found with id: " + id));
            }

            // Update fields
            if (doctorDetails.getSpecialization() != null) {
                doctor.setSpecialization(doctorDetails.getSpecialization());
            }
            if (doctorDetails.getAvailableDays() != null) {
                doctor.setAvailableDays(doctorDetails.getAvailableDays());
            }
            if (doctorDetails.getSlotDurationMins() > 0) {
                doctor.setSlotDurationMins(doctorDetails.getSlotDurationMins());
            }

            doctorService.save(doctor);
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Doctor updated successfully");
            response.put("data", doctor);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return handleException("Failed to update doctor", e);
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
