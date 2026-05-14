package com.smartclinic.controller;

import com.smartclinic.model.Appointment;
import com.smartclinic.model.AppointmentWaitlist;
import com.smartclinic.model.User;
import com.smartclinic.service.AuditLogService;
import com.smartclinic.service.AppointmentService;
import com.smartclinic.service.AppointmentWaitlistService;
import com.smartclinic.service.DoctorService;
import com.smartclinic.service.PatientService;
import com.smartclinic.service.ReminderLogService;
import com.smartclinic.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
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

    @Autowired
    private AuditLogService auditLogService;

    @Autowired
    private UserService userService;

    @Autowired
    private AppointmentWaitlistService appointmentWaitlistService;

    @Autowired
    private ReminderLogService reminderLogService;

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
            @RequestParam(value = "slotTime", required = false) String slotTimeStr,
            @RequestParam(value = "emergencyOverride", required = false) String emergencyOverride,
            @RequestParam(value = "overrideSlotTime", required = false) String overrideSlotTime,
            @RequestParam(value = "overrideReason", required = false) String overrideReason,
            HttpServletRequest request) {

        try {
            boolean useOverride = emergencyOverride != null;
            String selectedSlot = useOverride ? overrideSlotTime : slotTimeStr;
            LocalDateTime slotTime = LocalDateTime.parse(selectedSlot);
            appointment.setSlotDatetime(slotTime);
            if (useOverride) {
                appointment.setPriority(Appointment.Priority.EMERGENCY);
                appointment.setNotes(appendNote(appointment.getNotes(), "Emergency override: " + clean(overrideReason)));
            }
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
        auditLogService.logAction(currentUser(), "BOOKED appointment for " + appointment.getPatient().getName(),
                "APPOINTMENT", appointment.getId(), request.getRemoteAddr());
        return "redirect:/appointments/queue?success=true";
    }

    @GetMapping("/queue")
    public String viewQueue(Model model) {
        model.addAttribute("doctors", doctorService.findAll());
        return "appointments/queue";
    }

    @PostMapping("/{id}/queue-state")
    public String updateQueueState(@PathVariable("id") Long appointmentId,
            @RequestParam("state") String state,
            HttpServletRequest request) {
        try {
            appointmentService.updateQueueState(appointmentId, state);
            auditLogService.logAction(currentUser(), "UPDATED queue state to " + state,
                    "APPOINTMENT", appointmentId, request.getRemoteAddr());
            return "redirect:/appointments/queue?queueUpdated=true";
        } catch (Exception e) {
            return "redirect:/appointments/queue?error=queue";
        }
    }

    @PostMapping("/{id}/reminders")
    public String sendReminder(@PathVariable("id") Long appointmentId,
            @RequestParam(value = "channel", defaultValue = "SMS") String channel,
            HttpServletRequest request) {
        try {
            reminderLogService.sendMockReminder(appointmentId, channel);
            auditLogService.logAction(currentUser(), "SENT mock " + channel + " reminder",
                    "APPOINTMENT", appointmentId, request.getRemoteAddr());
            return "redirect:/appointments/queue?reminderSent=true";
        } catch (Exception e) {
            return "redirect:/appointments/queue?error=reminder";
        }
    }

    @GetMapping("/reminders")
    public String reminders(Model model) {
        model.addAttribute("reminders", reminderLogService.findAll());
        return "appointments/reminders";
    }

    @GetMapping("/calendar")
    public String calendar(@RequestParam(value = "date", required = false) String dateParam, Model model) {
        LocalDate date = dateParam == null || dateParam.trim().isEmpty()
                ? LocalDate.now()
                : LocalDate.parse(dateParam);
        model.addAttribute("selectedDate", date);
        model.addAttribute("appointments", appointmentService.getAppointmentsByDate(date));
        return "appointments/calendar";
    }

    @GetMapping("/waitlist")
    public String waitlist(@RequestParam(value = "status", required = false) String statusParam,
            @RequestParam(value = "patientId", required = false) Long patientId,
            @RequestParam(value = "doctorId", required = false) Long doctorId,
            @RequestParam(value = "date", required = false) String date,
            @RequestParam(value = "priority", required = false) Appointment.Priority priority,
            @RequestParam(value = "page", required = false) Integer page,
            Model model) {
        AppointmentWaitlist.Status status = parseWaitlistStatus(statusParam);
        java.util.List<AppointmentWaitlist> entries = appointmentWaitlistService.findByStatus(status);
        com.smartclinic.util.PageSlice<AppointmentWaitlist> pageSlice =
                com.smartclinic.util.PaginationUtil.paginate(entries, page, 10);
        model.addAttribute("entries", pageSlice.getItems());
        model.addAttribute("pageSlice", pageSlice);
        model.addAttribute("patients", patientService.findAll());
        model.addAttribute("doctors", doctorService.findAll());
        model.addAttribute("selectedStatus", status);
        model.addAttribute("selectedPatientId", patientId);
        model.addAttribute("selectedDoctorId", doctorId);
        model.addAttribute("selectedDate", date);
        model.addAttribute("selectedPriority", priority == null ? Appointment.Priority.NORMAL : priority);
        return "appointments/waitlist";
    }

    @PostMapping("/waitlist")
    public String addWaitlistEntry(@RequestParam("patientId") Long patientId,
            @RequestParam("doctorId") Long doctorId,
            @RequestParam("requestedDate") String requestedDate,
            @RequestParam("priority") Appointment.Priority priority,
            @RequestParam(value = "notes", required = false) String notes,
            HttpServletRequest request) {
        AppointmentWaitlist entry = appointmentWaitlistService.addEntry(
                patientId,
                doctorId,
                LocalDate.parse(requestedDate),
                priority,
                notes);
        auditLogService.logAction(currentUser(), "ADDED waitlist entry #" + entry.getId(),
                "WAITLIST", entry.getId(), request.getRemoteAddr());
        return "redirect:/appointments/waitlist?added=true";
    }

    @PostMapping("/waitlist/{id}/status")
    public String updateWaitlistStatus(@PathVariable("id") Long waitlistId,
            @RequestParam("status") AppointmentWaitlist.Status status) {
        appointmentWaitlistService.updateStatus(waitlistId, status);
        return "redirect:/appointments/waitlist?updated=true";
    }

    @GetMapping("/{id}/timeline")
    public String appointmentTimeline(@PathVariable("id") Long appointmentId, Model model) {
        Appointment appointment = appointmentService.findById(appointmentId);
        if (appointment == null) {
            return "redirect:/appointments/queue?error=not_found";
        }

        model.addAttribute("appointment", appointment);
        model.addAttribute("logs", auditLogService.findByEntity("APPOINTMENT", appointmentId));
        return "appointments/timeline";
    }

    @GetMapping("/{id}/reschedule")
    public String showRescheduleForm(@PathVariable("id") Long appointmentId, Model model) {
        Appointment appointment = appointmentService.findById(appointmentId);
        if (appointment == null) {
            return "redirect:/appointments/queue?error=not_found";
        }

        model.addAttribute("appointment", appointment);
        model.addAttribute("doctors", doctorService.findAll());
        return "appointments/reschedule";
    }

    @PostMapping("/{id}/reschedule")
    public String rescheduleAppointment(@PathVariable("id") Long appointmentId,
            @RequestParam("doctorId") Long doctorId,
            @RequestParam("slotTime") String slotTimeStr,
            @RequestParam("priority") Appointment.Priority priority,
            @RequestParam(value = "notes", required = false) String notes,
            HttpServletRequest request) {
        try {
            LocalDateTime slotTime = LocalDateTime.parse(slotTimeStr);
            appointmentService.rescheduleAppointment(appointmentId, doctorId, slotTime, priority, notes);
            auditLogService.logAction(currentUser(), "RESCHEDULED appointment to " + slotTime,
                    "APPOINTMENT", appointmentId, request.getRemoteAddr());
            return "redirect:/appointments/queue?rescheduled=true";
        } catch (Exception e) {
            return "redirect:/appointments/" + appointmentId + "/reschedule?error=invalid";
        }
    }

    @PostMapping("/{id}/cancel")
    public String cancelAppointment(@PathVariable("id") Long appointmentId,
            @RequestParam(value = "reason", required = false) String reason,
            HttpServletRequest request) {
        try {
            Appointment appointment = appointmentService.findById(appointmentId);
            appointmentService.cancelAppointment(appointmentId, reason);
            auditLogService.logAction(currentUser(), "CANCELLED appointment" + formatReason(reason),
                    "APPOINTMENT", appointmentId, request.getRemoteAddr());
            AppointmentWaitlist notified = appointmentWaitlistService.notifyFirstForSlot(
                    appointment.getDoctor().getId(),
                    appointment.getSlotDatetime().toLocalDate());
            if (notified != null) {
                auditLogService.logAction(currentUser(), "NOTIFIED waitlist entry #" + notified.getId(),
                        "APPOINTMENT", appointmentId, request.getRemoteAddr());
                return "redirect:/appointments/queue?cancelled=true&waitlistNotified=true";
            }
            return "redirect:/appointments/queue?cancelled=true";
        } catch (Exception e) {
            return "redirect:/appointments/queue?error=not_found";
        }
    }

    private User currentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
            return null;
        }
        return userService.findByEmail(auth.getName());
    }

    private String formatReason(String reason) {
        if (reason == null || reason.trim().isEmpty()) {
            return "";
        }
        return " - " + reason.trim();
    }

    private String appendNote(String currentNotes, String newNote) {
        if (newNote == null || newNote.trim().isEmpty()) {
            return currentNotes;
        }
        if (currentNotes == null || currentNotes.trim().isEmpty()) {
            return newNote.trim();
        }
        return currentNotes.trim() + "\n" + newNote.trim();
    }

    private String clean(String value) {
        return value == null ? "" : value.trim();
    }

    private AppointmentWaitlist.Status parseWaitlistStatus(String statusParam) {
        if (statusParam == null || statusParam.trim().isEmpty()) {
            return null;
        }
        return AppointmentWaitlist.Status.valueOf(statusParam);
    }
}
