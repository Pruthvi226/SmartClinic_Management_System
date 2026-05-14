package com.smartclinic.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private com.smartclinic.service.DoctorService doctorService;

    @Autowired
    private com.smartclinic.service.UserService userService;

    @Autowired
    private com.smartclinic.service.PatientService patientService;

    @Autowired
    private com.smartclinic.service.AuditLogService auditLogService;

    @Autowired
    private org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("totalPatients", patientService.findAll().size());
        model.addAttribute("totalDoctors", doctorService.findAll().size());
        return "admin/dashboard";
    }

    @GetMapping("/doctors")
    public String listDoctors(Model model) {
        model.addAttribute("doctors", doctorService.findAll());
        return "admin/doctors";
    }

    @GetMapping("/doctors/add")
    public String showAddDoctorForm(Model model) {
        model.addAttribute("doctor", new com.smartclinic.model.Doctor());
        return "admin/doctor-form";
    }

    @PostMapping("/doctors/add")
    public String addDoctor(@ModelAttribute("doctor") com.smartclinic.model.Doctor doctor,
            @RequestParam("email") String email, @RequestParam("password") String password,
            @RequestParam("name") String name) {
        com.smartclinic.model.User user = new com.smartclinic.model.User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(com.smartclinic.model.User.Role.DOCTOR);
        userService.save(user);

        doctor.setUser(user);
        doctorService.save(doctor);
        return "redirect:/admin/doctors?success=true";
    }

    @GetMapping("/audit-log")
    public String auditLog(Model model) {
        model.addAttribute("logs", auditLogService.findAll());
        return "admin/audit-log";
    }
}
