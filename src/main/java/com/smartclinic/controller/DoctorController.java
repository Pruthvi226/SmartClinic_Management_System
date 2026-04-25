package com.smartclinic.controller;

import com.smartclinic.model.Appointment;
import com.smartclinic.model.Doctor;
import com.smartclinic.model.Prescription;
import com.smartclinic.model.PrescriptionItem;
import com.smartclinic.service.AppointmentService;
import com.smartclinic.service.BillingService;
import com.smartclinic.service.DoctorService;
import com.smartclinic.service.PrescriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@Controller
@RequestMapping("/doctor")
public class DoctorController {

    @Autowired
    private AppointmentService appointmentService;
    
    @Autowired
    private DoctorService doctorService;
    
    @Autowired
    private PrescriptionService prescriptionService;
    
    @Autowired
    private BillingService billingService;

    @GetMapping("/schedule")
    public String mySchedule(Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Doctor doctor = doctorService.findAll().stream().filter(d -> d.getUser().getEmail().equals(username)).findFirst().orElse(null);
        if (doctor != null) {
            model.addAttribute("appointments", appointmentService.getUpcomingForDoctor(doctor.getId()));
            model.addAttribute("doctor", doctor);
        }
        return "doctor/schedule";
    }

    @GetMapping("/consult/{id}")
    public String consultForm(@PathVariable("id") Long id, Model model) {
        Appointment appt = appointmentService.findById(id);
        model.addAttribute("appointment", appt);
        Prescription p = new Prescription();
        model.addAttribute("prescription", p);
        return "doctor/consult";
    }

    @PostMapping("/consult/{id}")
    public String processConsultation(@PathVariable("id") Long appointmentId, 
            @ModelAttribute("prescription") Prescription prescription) {
        Appointment appointment = appointmentService.findById(appointmentId);
        prescription.setAppointment(appointment);
        prescription.setDoctor(appointment.getDoctor());
        prescription.setPatient(appointment.getPatient());
        
        if (prescription.getItems() != null) {
            for(PrescriptionItem item : prescription.getItems()) {
                item.setPrescription(prescription);
            }
        }
        
        prescriptionService.createPrescription(prescription);
        appointmentService.updateStatus(appointmentId, Appointment.Status.COMPLETED);
        
        billingService.generateBill(appointmentId);
        
        return "redirect:/doctor/schedule?completed=true";
    }
}
