package com.smartclinic.controller;

import com.smartclinic.model.Patient;
import com.smartclinic.service.AppointmentService;
import com.smartclinic.service.PatientService;
import com.smartclinic.service.PrescriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/patients")
public class PatientController {

    @Autowired
    private PatientService patientService;
    
    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private PrescriptionService prescriptionService;

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("patient", new Patient());
        return "patients/register";
    }

    @PostMapping("/register")
    public String processRegistration(@Valid @ModelAttribute("patient") Patient patient, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "patients/register";
        }
        patientService.registerPatient(patient);
        return "redirect:/patients/search?success=true";
    }

    @GetMapping("/search")
    public String searchPatients(@RequestParam(value = "keyword", required = false) String keyword, Model model) {
        if (keyword != null && !keyword.trim().isEmpty()) {
            model.addAttribute("patients", patientService.searchPatients(keyword));
        } else {
            model.addAttribute("patients", patientService.findAll());
        }
        return "patients/search";
    }

    @GetMapping("/{id}/history")
    public String patientHistory(@PathVariable("id") Long id, Model model) {
        model.addAttribute("patient", patientService.findById(id));
        model.addAttribute("appointments", appointmentService.getPatientHistory(id));
        model.addAttribute("prescriptions", prescriptionService.getPatientPrescriptions(id));
        return "patients/history";
    }
}
