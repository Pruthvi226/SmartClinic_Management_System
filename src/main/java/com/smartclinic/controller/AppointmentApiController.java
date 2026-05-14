package com.smartclinic.controller;

import com.smartclinic.model.Appointment;
import com.smartclinic.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentApiController {

    @Autowired
    private AppointmentService appointmentService;

    /**
     * Get available time slots for a doctor on a specific date
     */
    @GetMapping("/slots")
    public ResponseEntity<Map<String, Object>> getAvailableSlots(
            @RequestParam("doctorId") Long doctorId,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam("priority") Appointment.Priority priority) {
        try {
            System.out.println("API: Fetching slots for doctor: " + doctorId + " on date: " + date);
            List<LocalDateTime> slots = appointmentService.getAvailableSlots(doctorId, date, priority);
            System.out.println("API: Found " + slots.size() + " slots");

            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("count", slots.size());
            response.put("data", slots);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return handleException("Failed to fetch available slots", e);
        }
    }

    /**
     * Get all appointments for a doctor (upcoming queue)
     */
    @GetMapping("/queue/{doctorId}")
    public ResponseEntity<Map<String, Object>> getDoctorQueue(@PathVariable("doctorId") Long doctorId) {
        try {
            List<Appointment> appointments = appointmentService.getUpcomingForDoctor(doctorId);
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("count", appointments.size());
            response.put("data", appointments);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return handleException("Failed to fetch doctor queue", e);
        }
    }

    /**
     * Get all appointments
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllAppointments() {
        try {
            // Note: Service doesn't have findAll() method, so we'll use a workaround
            // In production, consider adding findAll() to AppointmentService
            Map<String, Object> response = new HashMap<>();
            response.put("status", "info");
            response.put("message", "Use specific endpoints like /queue/{doctorId} or /patient/{patientId}");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return handleException("Failed to fetch appointments", e);
        }
    }

    /**
     * Get appointment by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getAppointmentById(@PathVariable Long id) {
        try {
            Appointment appointment = appointmentService.findById(id);
            if (appointment == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(createErrorResponse("Appointment not found with id: " + id));
            }
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("data", appointment);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return handleException("Failed to fetch appointment", e);
        }
    }

    /**
     * Get all appointments for a patient
     */
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<Map<String, Object>> getPatientAppointments(@PathVariable Long patientId) {
        try {
            List<Appointment> appointments = appointmentService.getPatientHistory(patientId);
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("count", appointments.size());
            response.put("data", appointments);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return handleException("Failed to fetch patient appointments", e);
        }
    }

    /**
     * Book a new appointment
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> bookAppointment(@Valid @RequestBody Appointment appointment) {
        try {
            if (appointment.getPatient() == null || appointment.getPatient().getId() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(createErrorResponse("Patient ID is required"));
            }
            if (appointment.getDoctor() == null || appointment.getDoctor().getId() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(createErrorResponse("Doctor ID is required"));
            }
            if (appointment.getSlotDatetime() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(createErrorResponse("Appointment date and time is required"));
            }

            appointmentService.bookAppointment(appointment);
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Appointment booked successfully");
            response.put("data", appointment);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return handleException("Failed to book appointment", e);
        }
    }

    /**
     * Update appointment status (SCHEDULED, COMPLETED, CANCELLED)
     */
    @PutMapping("/{id}/status")
    public ResponseEntity<Map<String, Object>> updateAppointmentStatus(
            @PathVariable Long id,
            @RequestParam Appointment.Status status) {
        try {
            Appointment appointment = appointmentService.findById(id);
            if (appointment == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(createErrorResponse("Appointment not found with id: " + id));
            }

            appointmentService.updateStatus(id, status);
            appointment = appointmentService.findById(id); // Reload to get updated data

            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Appointment status updated successfully");
            response.put("data", appointment);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return handleException("Failed to update appointment status", e);
        }
    }

    /**
     * Update appointment details
     */
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateAppointment(
            @PathVariable Long id,
            @Valid @RequestBody Appointment appointmentDetails) {
        try {
            Appointment appointment = appointmentService.findById(id);
            if (appointment == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(createErrorResponse("Appointment not found with id: " + id));
            }

            // Update fields
            if (appointmentDetails.getSlotDatetime() != null) {
                appointment.setSlotDatetime(appointmentDetails.getSlotDatetime());
            }
            if (appointmentDetails.getNotes() != null) {
                appointment.setNotes(appointmentDetails.getNotes());
            }
            if (appointmentDetails.getPriority() != null) {
                appointment.setPriority(appointmentDetails.getPriority());
            }

            appointmentService.bookAppointment(appointment);
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Appointment updated successfully");
            response.put("data", appointment);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return handleException("Failed to update appointment", e);
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
