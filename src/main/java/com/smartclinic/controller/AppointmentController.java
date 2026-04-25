package com.smartclinic.controller;

import com.smartclinic.model.Appointment;
import com.smartclinic.model.Doctor;
import com.smartclinic.service.AppointmentService;
import com.smartclinic.service.DoctorService;
import com.smartclinic.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

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

    @PostMapping("/book")
    public String bookAppointment(@ModelAttribute("appointment") Appointment appointment, 
            @RequestParam("doctorId") Long doctorId, @RequestParam(value = "patientId", required = false) Long patientId,
            @RequestParam("slotTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime slotTime) {
        
        appointment.setDoctor(doctorService.findById(doctorId));
        if(patientId != null) {
            appointment.setPatient(patientService.findById(patientId));
        }
        appointment.setSlotDatetime(slotTime);
        appointment.setStatus(Appointment.Status.SCHEDULED);
        
        appointmentService.bookAppointment(appointment);
        return "redirect:/appointments/queue?success=true";
    }

    @GetMapping("/queue")
    public String viewQueue(Model model) {
        model.addAttribute("doctors", doctorService.findAll());
        return "appointments/queue";
    }
    
    @ResponseBody
    @GetMapping("/api/slots")
    public List<LocalDateTime> getAvailableSlots(@RequestParam("doctorId") Long doctorId,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam("priority") Appointment.Priority priority) {
        return appointmentService.getAvailableSlots(doctorId, date, priority);
    }
    
    @ResponseBody
    @GetMapping("/api/queue/{doctorId}")
    public List<Appointment> getDoctorQueue(@PathVariable("doctorId") Long doctorId) {
        return appointmentService.getUpcomingForDoctor(doctorId);
    }
}
