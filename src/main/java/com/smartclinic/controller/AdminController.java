package com.smartclinic.controller;

import com.smartclinic.model.Department;
import com.smartclinic.model.SystemSetting;
import com.smartclinic.model.User;
import com.smartclinic.service.DepartmentService;
import com.smartclinic.service.SystemSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    private com.smartclinic.service.MedicineInventoryService medicineInventoryService;

    @Autowired
    private com.smartclinic.service.DoctorLeaveService doctorLeaveService;

    @Autowired
    private com.smartclinic.service.AppointmentService appointmentService;

    @Autowired
    private com.smartclinic.service.BillingService billingService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private SystemSettingService systemSettingService;

    @Autowired
    private org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("totalPatients", patientService.findAll().size());
        model.addAttribute("totalDoctors", doctorService.findAll().size());
        model.addAttribute("lowStockCount", medicineInventoryService.findLowStock().size());
        model.addAttribute("auditCount", auditLogService.findAll().size());
        model.addAttribute("userCount", userService.findAll().size());
        return "admin/dashboard";
    }

    @GetMapping("/users")
    public String users(Model model) {
        model.addAttribute("users", userService.findAll());
        model.addAttribute("user", new User());
        return "admin/users";
    }

    @PostMapping("/users")
    public String createUser(@RequestParam("name") String name,
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            @RequestParam("role") User.Role role) {
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(role);
        user.setActive(true);
        userService.save(user);
        return "redirect:/admin/users?created=true";
    }

    @GetMapping("/users/{id}/edit")
    public String editUser(@PathVariable("id") Long id, Model model) {
        User user = userService.findById(id);
        if (user == null) {
            return "redirect:/admin/users?error=not_found";
        }
        model.addAttribute("users", userService.findAll());
        model.addAttribute("user", user);
        model.addAttribute("editMode", true);
        return "admin/users";
    }

    @PostMapping("/users/{id}/edit")
    public String updateUser(@PathVariable("id") Long id,
            @RequestParam("name") String name,
            @RequestParam("email") String email,
            @RequestParam(value = "password", required = false) String password,
            @RequestParam("role") User.Role role,
            @RequestParam(value = "active", required = false) String active) {
        User user = userService.findById(id);
        if (user == null) {
            return "redirect:/admin/users?error=not_found";
        }
        user.setName(name);
        user.setEmail(email);
        user.setRole(role);
        user.setActive(active != null);
        if (password != null && !password.trim().isEmpty()) {
            user.setPassword(passwordEncoder.encode(password));
        }
        userService.save(user);
        return "redirect:/admin/users?updated=true";
    }

    @PostMapping("/users/{id}/toggle")
    public String toggleUser(@PathVariable("id") Long id) {
        userService.toggleActive(id);
        return "redirect:/admin/users?toggled=true";
    }

    @GetMapping("/doctors")
    public String listDoctors(Model model) {
        model.addAttribute("doctors", doctorService.findAll());
        return "admin/doctors";
    }

    @GetMapping("/doctors/add")
    public String showAddDoctorForm(Model model) {
        model.addAttribute("doctor", new com.smartclinic.model.Doctor());
        model.addAttribute("formAction", "/admin/doctors/add");
        model.addAttribute("formTitle", "Register New Doctor");
        model.addAttribute("submitLabel", "Register Doctor");
        model.addAttribute("editMode", false);
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

    @GetMapping("/doctors/{id}/edit")
    public String showEditDoctorForm(@PathVariable("id") Long id, Model model) {
        com.smartclinic.model.Doctor doctor = doctorService.findById(id);
        if (doctor == null) {
            return "redirect:/admin/doctors?error=not_found";
        }

        model.addAttribute("doctor", doctor);
        model.addAttribute("formAction", "/admin/doctors/" + id + "/edit");
        model.addAttribute("formTitle", "Edit Doctor Availability");
        model.addAttribute("submitLabel", "Save Changes");
        model.addAttribute("editMode", true);
        return "admin/doctor-form";
    }

    @PostMapping("/doctors/{id}/edit")
    public String updateDoctor(@PathVariable("id") Long id,
            @RequestParam("email") String email,
            @RequestParam(value = "password", required = false) String password,
            @RequestParam("name") String name,
            @RequestParam("specialization") String specialization,
            @RequestParam("availableDays") String availableDays,
            @RequestParam("slotDurationMins") Integer slotDurationMins) {
        com.smartclinic.model.Doctor doctor = doctorService.findById(id);
        if (doctor == null) {
            return "redirect:/admin/doctors?error=not_found";
        }

        com.smartclinic.model.User user = doctor.getUser();
        user.setName(name);
        user.setEmail(email);
        if (password != null && !password.trim().isEmpty()) {
            user.setPassword(passwordEncoder.encode(password));
        }
        userService.save(user);

        doctor.setSpecialization(specialization);
        doctor.setAvailableDays(availableDays);
        doctor.setSlotDurationMins(slotDurationMins);
        doctorService.save(doctor);
        return "redirect:/admin/doctors?updated=true";
    }

    @GetMapping("/doctors/{id}/leaves")
    public String doctorLeaves(@PathVariable("id") Long id, Model model) {
        com.smartclinic.model.Doctor doctor = doctorService.findById(id);
        if (doctor == null) {
            return "redirect:/admin/doctors?error=not_found";
        }

        model.addAttribute("doctor", doctor);
        model.addAttribute("leaves", doctorLeaveService.findByDoctorId(id));
        return "admin/doctor-leaves";
    }

    @PostMapping("/doctors/{id}/leaves")
    public String addDoctorLeave(@PathVariable("id") Long id,
            @RequestParam("leaveDate") String leaveDate,
            @RequestParam(value = "reason", required = false) String reason) {
        doctorLeaveService.addLeave(id, LocalDate.parse(leaveDate), reason);
        return "redirect:/admin/doctors/" + id + "/leaves?added=true";
    }

    @PostMapping("/doctors/{doctorId}/leaves/{leaveId}/delete")
    public String deleteDoctorLeave(@PathVariable("doctorId") Long doctorId,
            @PathVariable("leaveId") Long leaveId) {
        doctorLeaveService.deleteLeave(leaveId);
        return "redirect:/admin/doctors/" + doctorId + "/leaves?deleted=true";
    }

    @GetMapping("/reports")
    public String reports(@RequestParam(value = "date", required = false) String dateParam, Model model) {
        LocalDate date = dateParam == null || dateParam.trim().isEmpty()
                ? LocalDate.now()
                : LocalDate.parse(dateParam);
        List<com.smartclinic.model.Appointment> appointments = appointmentService.getAppointmentsByDate(date);
        List<com.smartclinic.model.Billing> bills = billingService.findAll();
        BigDecimal totalRevenue = bills.stream()
                .filter(bill -> "PAID".equals(bill.getPaymentStatus()) || "PARTIAL".equals(bill.getPaymentStatus()))
                .map(com.smartclinic.model.Billing::getPaidAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        long completed = appointments.stream()
                .filter(appointment -> appointment.getStatus() == com.smartclinic.model.Appointment.Status.COMPLETED)
                .count();

        model.addAttribute("reportDate", date);
        model.addAttribute("appointments", appointments);
        model.addAttribute("appointmentCount", appointments.size());
        model.addAttribute("completedCount", completed);
        model.addAttribute("paidRevenue", totalRevenue);
        model.addAttribute("pendingBills", bills.stream()
                .filter(bill -> !"PAID".equals(bill.getPaymentStatus()))
                .collect(Collectors.toList()));
        model.addAttribute("lowStockMedicines", medicineInventoryService.findLowStock());
        return "admin/reports";
    }

    @GetMapping("/revenue")
    public String revenue(@RequestParam(value = "from", required = false) String from,
            @RequestParam(value = "to", required = false) String to,
            Model model) {
        LocalDate fromDate = from == null || from.trim().isEmpty() ? LocalDate.now().minusDays(30) : LocalDate.parse(from);
        LocalDate toDate = to == null || to.trim().isEmpty() ? LocalDate.now() : LocalDate.parse(to);
        List<com.smartclinic.model.Billing> bills = billingService.findAll().stream()
                .filter(bill -> bill.getGeneratedAt() != null)
                .filter(bill -> !bill.getGeneratedAt().toLocalDateTime().toLocalDate().isBefore(fromDate))
                .filter(bill -> !bill.getGeneratedAt().toLocalDateTime().toLocalDate().isAfter(toDate))
                .collect(Collectors.toList());

        BigDecimal paid = bills.stream().map(com.smartclinic.model.Billing::getPaidAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal discounts = bills.stream().map(com.smartclinic.model.Billing::getDiscountAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal refunds = bills.stream().map(com.smartclinic.model.Billing::getRefundAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        long insuranceClaims = bills.stream()
                .filter(bill -> bill.getInsuranceStatus() != null && !"NOT_APPLICABLE".equals(bill.getInsuranceStatus()))
                .count();

        model.addAttribute("from", fromDate);
        model.addAttribute("to", toDate);
        model.addAttribute("bills", bills);
        model.addAttribute("paidRevenue", paid);
        model.addAttribute("discounts", discounts);
        model.addAttribute("refunds", refunds);
        model.addAttribute("netRevenue", paid.subtract(refunds));
        model.addAttribute("insuranceClaims", insuranceClaims);
        return "admin/revenue";
    }

    @GetMapping("/departments")
    public String departments(Model model) {
        model.addAttribute("departments", departmentService.findAll());
        model.addAttribute("department", new Department());
        return "admin/departments";
    }

    @PostMapping("/departments")
    public String saveDepartment(@ModelAttribute("department") Department department) {
        departmentService.save(department);
        return "redirect:/admin/departments?saved=true";
    }

    @PostMapping("/departments/{id}/toggle")
    public String toggleDepartment(@PathVariable("id") Long id) {
        departmentService.toggleActive(id);
        return "redirect:/admin/departments?toggled=true";
    }

    @GetMapping("/settings")
    public String settings(Model model) {
        model.addAttribute("settings", settingsMap());
        return "admin/settings";
    }

    @PostMapping("/settings")
    public String saveSettings(@RequestParam("normalFee") String normalFee,
            @RequestParam("emergencyFee") String emergencyFee,
            @RequestParam("seniorFee") String seniorFee,
            @RequestParam("taxRate") String taxRate,
            @RequestParam("hospitalName") String hospitalName) {
        systemSettingService.save("consultation.fee.normal", normalFee, "Normal consultation fee");
        systemSettingService.save("consultation.fee.emergency", emergencyFee, "Emergency consultation fee");
        systemSettingService.save("consultation.fee.senior", seniorFee, "Senior citizen consultation fee");
        systemSettingService.save("tax.rate", taxRate, "Tax rate used when stored procedure is unavailable");
        systemSettingService.save("hospital.name", hospitalName, "Hospital display name");
        return "redirect:/admin/settings?saved=true";
    }

    @GetMapping("/export/patients")
    public ResponseEntity<byte[]> exportPatients() {
        StringBuilder csv = new StringBuilder("id,name,phone,email,blood_group,allergies\n");
        patientService.findAll().forEach(patient -> csv.append(patient.getId()).append(',')
                .append(csv(patient.getName())).append(',')
                .append(csv(patient.getPhone())).append(',')
                .append(csv(patient.getEmail())).append(',')
                .append(csv(patient.getBloodGroup())).append(',')
                .append(csv(patient.getAllergies())).append('\n'));
        return csv("patients.csv", csv.toString());
    }

    @GetMapping("/export/billing")
    public ResponseEntity<byte[]> exportBilling() {
        StringBuilder csv = new StringBuilder("invoice,patient,total,paid,discount,refund,status,insurance_status\n");
        billingService.findAll().forEach(bill -> csv.append("INV-").append(bill.getId()).append(',')
                .append(csv(bill.getAppointment().getPatient().getName())).append(',')
                .append(bill.getTotal()).append(',')
                .append(bill.getPaidAmount()).append(',')
                .append(bill.getDiscountAmount()).append(',')
                .append(bill.getRefundAmount()).append(',')
                .append(csv(bill.getPaymentStatus())).append(',')
                .append(csv(bill.getInsuranceStatus())).append('\n'));
        return csv("billing.csv", csv.toString());
    }

    @GetMapping("/export/audit")
    public ResponseEntity<byte[]> exportAudit() {
        StringBuilder csv = new StringBuilder("id,user,action,entity_type,entity_id,timestamp,ip\n");
        auditLogService.findAll().forEach(log -> csv.append(log.getId()).append(',')
                .append(csv(log.getUser() == null ? "system" : log.getUser().getEmail())).append(',')
                .append(csv(log.getAction())).append(',')
                .append(csv(log.getEntityType())).append(',')
                .append(log.getEntityId()).append(',')
                .append(csv(log.getTimestamp() == null ? "" : log.getTimestamp().toString())).append(',')
                .append(csv(log.getIpAddress())).append('\n'));
        return csv("audit-log.csv", csv.toString());
    }

    @GetMapping("/audit-log")
    public String auditLog(@RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "entityType", required = false) String entityType,
            @RequestParam(value = "user", required = false) String user,
            @RequestParam(value = "from", required = false) String from,
            @RequestParam(value = "to", required = false) String to,
            @RequestParam(value = "page", required = false) Integer page,
            Model model) {
        List<com.smartclinic.model.AuditLog> logs = auditLogService.findAll().stream()
                .filter(log -> keyword == null || keyword.trim().isEmpty()
                        || log.getAction().toLowerCase().contains(keyword.trim().toLowerCase()))
                .filter(log -> entityType == null || entityType.trim().isEmpty()
                        || entityType.equals(log.getEntityType()))
                .filter(log -> user == null || user.trim().isEmpty()
                        || (log.getUser() != null && log.getUser().getEmail().toLowerCase().contains(user.trim().toLowerCase())))
                .filter(log -> from == null || from.trim().isEmpty()
                        || (log.getTimestamp() != null && !log.getTimestamp().toLocalDateTime().toLocalDate().isBefore(LocalDate.parse(from))))
                .filter(log -> to == null || to.trim().isEmpty()
                        || (log.getTimestamp() != null && !log.getTimestamp().toLocalDateTime().toLocalDate().isAfter(LocalDate.parse(to))))
                .collect(Collectors.toList());
        com.smartclinic.util.PageSlice<com.smartclinic.model.AuditLog> pageSlice =
                com.smartclinic.util.PaginationUtil.paginate(logs, page, 10);

        model.addAttribute("logs", pageSlice.getItems());
        model.addAttribute("pageSlice", pageSlice);
        model.addAttribute("keyword", keyword);
        model.addAttribute("entityType", entityType);
        model.addAttribute("user", user);
        model.addAttribute("from", from);
        model.addAttribute("to", to);
        return "admin/audit-log";
    }

    private Map<String, String> settingsMap() {
        Map<String, String> values = new HashMap<>();
        List<SystemSetting> settings = systemSettingService.findAll();
        for (SystemSetting setting : settings) {
            values.put(setting.getSettingKey(), setting.getSettingValue());
        }
        values.putIfAbsent("consultation.fee.normal", systemSettingService.getValue("consultation.fee.normal", "100.00"));
        values.putIfAbsent("consultation.fee.emergency", systemSettingService.getValue("consultation.fee.emergency", "200.00"));
        values.putIfAbsent("consultation.fee.senior", systemSettingService.getValue("consultation.fee.senior", "80.00"));
        values.putIfAbsent("tax.rate", systemSettingService.getValue("tax.rate", "0.18"));
        values.putIfAbsent("hospital.name", systemSettingService.getValue("hospital.name", "SmartClinic Hospital"));
        return values;
    }

    private ResponseEntity<byte[]> csv(String filename, String csv) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("text", "csv", StandardCharsets.UTF_8));
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename);
        return ResponseEntity.ok().headers(headers).body(csv.getBytes(StandardCharsets.UTF_8));
    }

    private String csv(String value) {
        if (value == null) {
            return "";
        }
        return "\"" + value.replace("\"", "\"\"") + "\"";
    }
}
