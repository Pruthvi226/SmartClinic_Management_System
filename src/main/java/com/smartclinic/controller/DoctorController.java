package com.smartclinic.controller;

import com.smartclinic.model.Appointment;
import com.smartclinic.model.Doctor;
import com.smartclinic.model.Prescription;
import com.smartclinic.model.PrescriptionItem;
import com.smartclinic.model.User;
import com.smartclinic.service.AuditLogService;
import com.smartclinic.service.AppointmentService;
import com.smartclinic.service.BillingService;
import com.smartclinic.service.DoctorService;
import com.smartclinic.service.PrescriptionService;
import com.smartclinic.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

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

    @Autowired
    private AuditLogService auditLogService;

    @Autowired
    private UserService userService;

    @InitBinder("prescription")
    public void initPrescriptionBinder(WebDataBinder binder) {
        binder.setDisallowedFields("id");
    }

    @GetMapping("/schedule")
    public String mySchedule(Model model) {
        Doctor doctor = currentDoctor();
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
        Prescription p = prescriptionService.findByAppointmentId(id);
        model.addAttribute("prescription", p == null ? new Prescription() : p);
        addConsultOptions(model);
        if (appt != null) {
            appointmentService.updateQueueState(id, "IN_CONSULTATION");
        }
        return "doctor/consult";
    }

    @PostMapping("/consult/{id}")
    public String processConsultation(@PathVariable("id") Long appointmentId, 
            @ModelAttribute("prescription") Prescription prescription,
            @RequestParam(value = "action", defaultValue = "COMPLETE") String action,
            HttpServletRequest request) {
        Appointment appointment = appointmentService.findById(appointmentId);
        prescription.setAppointment(appointment);
        prescription.setDoctor(appointment.getDoctor());
        prescription.setPatient(appointment.getPatient());
        
        if (prescription.getItems() != null) {
            for(PrescriptionItem item : prescription.getItems()) {
                item.setPrescription(prescription);
            }
        }

        boolean draft = "DRAFT".equalsIgnoreCase(action);
        prescriptionService.saveConsultation(prescription, draft);
        if (draft) {
            appointmentService.updateQueueState(appointmentId, "DRAFT_SAVED");
            auditLogService.logAction(currentUser(), "SAVED consultation draft",
                    "APPOINTMENT", appointmentId, request.getRemoteAddr());
            return "redirect:/doctor/schedule?draftSaved=true";
        }

        appointmentService.updateStatus(appointmentId, Appointment.Status.COMPLETED);
        appointmentService.updateQueueState(appointmentId, "COMPLETED");

        billingService.generateBill(appointmentId);
        auditLogService.logAction(currentUser(), "COMPLETED consultation with vitals, labs, prescription and bill",
                "APPOINTMENT", appointmentId, request.getRemoteAddr());
        
        return "redirect:/doctor/schedule?completed=true";
    }

    @GetMapping("/completed")
    public String completedConsultations(Model model) {
        Doctor doctor = currentDoctor();
        model.addAttribute("doctor", doctor);
        model.addAttribute("prescriptions", doctor == null ? java.util.Collections.emptyList()
                : prescriptionService.getDoctorPrescriptions(doctor.getId()));
        return "doctor/completed";
    }

    @GetMapping("/profile")
    public String profile(Model model) {
        Doctor doctor = currentDoctor();
        model.addAttribute("doctor", doctor);
        return "doctor/profile";
    }

    private void addConsultOptions(Model model) {
        List<String> templates = Arrays.asList(
                "General OPD",
                "Fever Review",
                "Hypertension Follow-up",
                "Cardiology Review",
                "Respiratory Assessment");
        List<String> diagnosisTags = Arrays.asList(
                "J00 Acute nasopharyngitis",
                "R50 Fever",
                "I10 Hypertension",
                "E11 Type 2 diabetes",
                "R07 Chest pain");
        List<String> prescriptionFavorites = Arrays.asList(
                "Fever Pack",
                "BP Follow-up Pack",
                "Gastric Protection",
                "Cardiac Starter Pack");
        model.addAttribute("clinicalTemplates", templates);
        model.addAttribute("diagnosisTags", diagnosisTags);
        model.addAttribute("prescriptionFavorites", prescriptionFavorites);
    }

    private Doctor currentDoctor() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return doctorService.findAll().stream()
                .filter(d -> d.getUser().getEmail().equals(username))
                .findFirst()
                .orElse(null);
    }

    private User currentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
            return null;
        }
        return userService.findByEmail(auth.getName());
    }
}
