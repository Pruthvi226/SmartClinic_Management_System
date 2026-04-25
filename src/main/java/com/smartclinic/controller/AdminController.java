package com.smartclinic.controller;

import com.smartclinic.service.AppointmentService;
import com.smartclinic.service.AuditLogService;
import com.smartclinic.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AuditLogService auditLogService;
    
    @Autowired
    private PatientService patientService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("totalPatients", patientService.findAll().size());
        return "admin/dashboard";
    }

    @GetMapping("/audit-log")
    public String auditLog(Model model) {
        model.addAttribute("logs", auditLogService.findAll());
        return "admin/audit-log";
    }
}
