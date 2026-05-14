package com.smartclinic.controller;

import com.smartclinic.model.Appointment;
import com.smartclinic.service.AppointmentService;
import com.smartclinic.service.DoctorService;
import com.smartclinic.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/appointments")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private PatientService patientService;

    @GetMapping("/book")
    public String showBookingForm(@RequestParam(value = "patientId", required = false) Long patientId, Model model) {
        model.addAttribute("doctors", doctorService.findAll());
        if (patientId != null) {
            model.addAttribute("patient", patientService.findById(patientId));
        } else {
            model.addAttribute("patients", patientService.findAll());
        }
        model.addAttribute("appointment", new Appointment());
        return "appointments/book";
    }

    @PostMapping(value = "/book")
    public String bookAppointment(@ModelAttribute("appointment") Appointment appointment,
            @RequestParam("doctorId") Long doctorId,
            @RequestParam(value = "patientId", required = false) Long patientId,
            @RequestParam("slotTime") String slotTimeStr) {

        try {
            LocalDateTime slotTime = LocalDateTime.parse(slotTimeStr);
            appointment.setSlotDatetime(slotTime);
        } catch (Exception e) {
            // Fallback for different formats if needed
            return "redirect:/appointments/book?error=invalid_date&patientId=" + patientId;
        }

        appointment.setDoctor(doctorService.findById(doctorId));
        if (patientId != null) {
            appointment.setPatient(patientService.findById(patientId));
        }
        appointment.setStatus(Appointment.Status.SCHEDULED);

        appointmentService.bookAppointment(appointment);
        return "redirect:/appointments/queue?success=true";
    }

    @GetMapping("/queue")
    public String viewQueue(Model model) {
        model.addAttribute("doctors", doctorService.findAll());
        return "appointments/queue";
    }
}
