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

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        Patient patient = patientService.findById(id);
        if (patient == null) {
            return "redirect:/patients/search?error=not_found";
        }
        model.addAttribute("patient", patient);
        model.addAttribute("editMode", true);
        return "patients/register";
    }

    @PostMapping("/{id}/edit")
    public String updatePatient(@PathVariable("id") Long id,
            @Valid @ModelAttribute("patient") Patient patient,
            BindingResult result,
            Model model) {
        if (result.hasErrors()) {
            model.addAttribute("editMode", true);
            return "patients/register";
        }
        patient.setId(id);
        patientService.updatePatient(patient);
        return "redirect:/patients/" + id + "/history?updated=true";
    }

    @GetMapping("/search")
    public String searchPatients(@RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "page", required = false) Integer page,
            Model model) {
        java.util.List<Patient> patients;
        if (keyword != null && !keyword.trim().isEmpty()) {
            patients = patientService.searchPatients(keyword);
        } else {
            patients = patientService.findAll();
        }
        com.smartclinic.util.PageSlice<Patient> pageSlice = com.smartclinic.util.PaginationUtil.paginate(patients, page, 10);
        model.addAttribute("patients", pageSlice.getItems());
        model.addAttribute("pageSlice", pageSlice);
        model.addAttribute("keyword", keyword);
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
