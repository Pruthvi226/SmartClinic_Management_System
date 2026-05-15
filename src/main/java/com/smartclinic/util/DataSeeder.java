package com.smartclinic.util;

import com.smartclinic.model.Appointment;
import com.smartclinic.model.Billing;
import com.smartclinic.model.Department;
import com.smartclinic.model.Doctor;
import com.smartclinic.model.MedicineInventory;
import com.smartclinic.model.Patient;
import com.smartclinic.model.Prescription;
import com.smartclinic.model.PrescriptionItem;
import com.smartclinic.model.User;
import com.smartclinic.service.AppointmentService;
import com.smartclinic.service.BillingService;
import com.smartclinic.service.DepartmentService;
import com.smartclinic.service.DoctorService;
import com.smartclinic.service.MedicineInventoryService;
import com.smartclinic.service.PatientService;
import com.smartclinic.service.PrescriptionService;
import com.smartclinic.service.ReminderLogService;
import com.smartclinic.service.StockMovementService;
import com.smartclinic.service.SystemSettingService;
import com.smartclinic.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Date;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Component
public class DataSeeder implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger logger = LoggerFactory.getLogger(DataSeeder.class);

    @Autowired
    private UserService userService;

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private PatientService patientService;

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private PrescriptionService prescriptionService;

    @Autowired
    private BillingService billingService;

    @Autowired
    private MedicineInventoryService medicineInventoryService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private SystemSettingService systemSettingService;

    @Autowired
    private ReminderLogService reminderLogService;

    @Autowired
    private StockMovementService stockMovementService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @SuppressWarnings("null")
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (event.getApplicationContext().getParent() == null) {
            seedData();
        }
    }

    private void seedData() {
        User admin = upsertUser("System Admin", "admin@smartclinic.com", "admin123", User.Role.ADMIN);
        User doctorUser = upsertUser("Dr. John Smith", "doctor@smartclinic.com", "doctor123", User.Role.DOCTOR);
        User cardioUser = upsertUser("Dr. Priya Nair", "cardio@smartclinic.com", "doctor123", User.Role.DOCTOR);
        upsertUser("Reception Desk", "reception@smartclinic.com", "reception123", User.Role.RECEPTIONIST);
        upsertUser("Pharmacy Desk", "pharmacy@smartclinic.com", "pharmacy123", User.Role.PHARMACIST);

        Doctor generalDoctor = upsertDoctor(doctorUser, "General Physician",
                "MONDAY,TUESDAY,WEDNESDAY,THURSDAY,FRIDAY", 30);
        Doctor cardioDoctor = upsertDoctor(cardioUser, "Cardiology", "MONDAY,WEDNESDAY,FRIDAY", 30);

        seedDepartmentsAndSettings();
        seedMedicineInventory();
        List<Patient> patients = seedPatients();
        seedAppointmentsAndClinicalData(patients, generalDoctor, cardioDoctor);
        seedOperationalDemoRecords();

        logger.info("Demo users ready. Admin login: {} / admin123", admin.getEmail());
    }

    private User upsertUser(String name, String email, String rawPassword, User.Role role) {
        User user = userService.findByEmail(email);
        if (user == null) {
            user = new User();
            user.setEmail(email);
            user.setPassword(passwordEncoder.encode(rawPassword));
        }

        user.setName(name);
        user.setRole(role);
        user.setActive(true);
        userService.save(user);
        return userService.findByEmail(email);
    }

    private Doctor upsertDoctor(User user, String specialization, String availableDays, int slotDurationMins) {
        Doctor doctor = doctorService.findByUserId(user.getId());
        if (doctor == null) {
            doctor = new Doctor();
            doctor.setUser(user);
            doctor.setSpecialization(specialization);
            doctor.setAvailableDays(availableDays);
            doctor.setSlotDurationMins(slotDurationMins);
            doctorService.save(doctor);
            return doctorService.findByUserId(user.getId());
        }

        if (doctor.getSpecialization() == null || doctor.getSpecialization().trim().isEmpty()) {
            doctor.setSpecialization(specialization);
        }
        if (doctor.getAvailableDays() == null || doctor.getAvailableDays().trim().isEmpty()) {
            doctor.setAvailableDays(availableDays);
        }
        if (doctor.getSlotDurationMins() == null || doctor.getSlotDurationMins() <= 0) {
            doctor.setSlotDurationMins(slotDurationMins);
        }
        doctorService.save(doctor);
        return doctorService.findByUserId(user.getId());
    }

    private void seedMedicineInventory() {
        upsertMedicine("Aspirin", "Cardiology", 120, 20, "2.00", "ASP-26-A",
                LocalDate.now().plusDays(180), "MediSupply India", "Clopidogrel");
        upsertMedicine("Atorvastatin", "Cardiology", 8, 10, "12.00", "ATO-26-B",
                LocalDate.now().plusDays(35), "CardioPharm", "Rosuvastatin");
        upsertMedicine("Pantoprazole", "Gastro", 45, 15, "6.00", "PAN-26-C",
                LocalDate.now().plusDays(90), "HealthKart Wholesale", "Omeprazole");
        upsertMedicine("Paracetamol", "General", 150, 25, "3.00", "PCM-26-D",
                LocalDate.now().plusDays(300), "MediSupply India", "Ibuprofen");
        upsertMedicine("Amoxicillin", "Antibiotic", 25, 10, "8.00", "AMX-26-E",
                LocalDate.now().plusDays(120), "Antibiotic House", "Azithromycin");
    }

    private void upsertMedicine(String name, String category, int stockQuantity, int reorderLevel, String unitPrice,
            LocalDate expiryDate, String supplierName, String substitutionName) {
        upsertMedicine(name, category, stockQuantity, reorderLevel, unitPrice, null, expiryDate, supplierName, substitutionName);
    }

    private void upsertMedicine(String name, String category, int stockQuantity, int reorderLevel, String unitPrice,
            String batchNumber, LocalDate expiryDate, String supplierName, String substitutionName) {
        MedicineInventory medicine = medicineInventoryService.findByName(name);
        if (medicine == null) {
            medicine = new MedicineInventory();
            medicine.setMedicineName(name);
            medicine.setStockQuantity(stockQuantity);
            medicine.setUnitPrice(new BigDecimal(unitPrice));
        }

        medicine.setCategory(category);
        if (medicine.getStockQuantity() == null || medicine.getStockQuantity() == 0) {
            medicine.setStockQuantity(stockQuantity);
        }
        medicine.setReorderLevel(reorderLevel);
        if (medicine.getUnitPrice().compareTo(BigDecimal.ZERO) == 0) {
            medicine.setUnitPrice(new BigDecimal(unitPrice));
        }
        medicine.setBatchNumber(batchNumber);
        medicine.setExpiryDate(expiryDate);
        medicine.setSupplierName(supplierName);
        medicine.setSubstitutionName(substitutionName);
        medicineInventoryService.save(medicine);
    }

    private void seedDepartmentsAndSettings() {
        upsertDepartment("General Physician", "Primary OPD and wellness consultations");
        upsertDepartment("Cardiology", "Heart health, ECG review, and lipid management");
        upsertDepartment("Emergency", "Urgent triage and emergency override workflow");

        systemSettingService.save("consultation.fee.normal", "100.00", "Normal consultation fee");
        systemSettingService.save("consultation.fee.emergency", "200.00", "Emergency consultation fee");
        systemSettingService.save("consultation.fee.senior", "80.00", "Senior citizen consultation fee");
        systemSettingService.save("tax.rate", "0.18", "Fallback tax rate when stored procedure is unavailable");
        systemSettingService.save("hospital.name", "SmartClinic Hospital", "Hospital display name");
    }

    private void upsertDepartment(String name, String description) {
        boolean exists = departmentService.findAll().stream()
                .anyMatch(department -> name.equalsIgnoreCase(department.getName()));
        if (exists) {
            return;
        }
        Department department = new Department();
        department.setName(name);
        department.setDescription(description);
        department.setActive(true);
        departmentService.save(department);
    }

    private List<Patient> seedPatients() {
        Patient aarav = upsertPatient("Aarav Mehta", "1992-08-12", 33, "MALE",
                "+91 98765 43210", "aarav.mehta@example.com", "O+", "Bandra West, Mumbai", "Penicillin");
        Patient neha = upsertPatient("Neha Sharma", "1988-03-24", 38, "FEMALE",
                "+91 99887 77665", "neha.sharma@example.com", "A+", "Indiranagar, Bengaluru", "Aspirin");
        Patient rohan = upsertPatient("Rohan Iyer", "1956-11-05", 69, "MALE",
                "+91 91234 56789", "rohan.iyer@example.com", "B+", "Adyar, Chennai", "None");
        Patient fatima = upsertPatient("Fatima Khan", "2001-01-17", 25, "FEMALE",
                "+91 90123 45678", "fatima.khan@example.com", "AB+", "Koregaon Park, Pune", "Sulfa");
        Patient vikram = upsertPatient("Vikram Rao", "1978-07-30", 47, "MALE",
                "+91 93456 78120", "vikram.rao@example.com", "O-", "Jubilee Hills, Hyderabad", "None");

        return Arrays.asList(aarav, neha, rohan, fatima, vikram);
    }

    private Patient upsertPatient(String name, String dob, int age, String gender, String phone,
            String email, String bloodGroup, String address, String allergies) {
        Patient patient = patientService.findAll().stream()
                .filter(p -> email.equalsIgnoreCase(p.getEmail()))
                .findFirst()
                .orElseGet(Patient::new);

        patient.setName(name);
        patient.setDob(Date.valueOf(dob));
        patient.setAge(age);
        patient.setGender(gender);
        patient.setPhone(phone);
        patient.setEmail(email);
        patient.setBloodGroup(bloodGroup);
        patient.setAddress(address);
        patient.setAllergies(allergies);
        patientService.registerPatient(patient);

        return patientService.findAll().stream()
                .filter(p -> email.equalsIgnoreCase(p.getEmail()))
                .findFirst()
                .orElse(patient);
    }

    private void seedAppointmentsAndClinicalData(List<Patient> patients, Doctor generalDoctor, Doctor cardioDoctor) {
        boolean appointmentsAlreadySeeded = patients.stream()
                .anyMatch(patient -> !appointmentService.getPatientHistory(patient.getId()).isEmpty());
        if (appointmentsAlreadySeeded) {
            return;
        }

        LocalDate tomorrow = LocalDate.now().plusDays(1);
        bookDemoAppointment(patients.get(0), generalDoctor, tomorrow.atTime(9, 0),
                Appointment.Status.SCHEDULED, Appointment.Priority.NORMAL, "Fever and body ache follow-up");
        bookDemoAppointment(patients.get(1), generalDoctor, tomorrow.atTime(9, 30),
                Appointment.Status.SCHEDULED, Appointment.Priority.EMERGENCY, "Shortness of breath triage");
        bookDemoAppointment(patients.get(2), cardioDoctor, tomorrow.atTime(10, 0),
                Appointment.Status.SCHEDULED, Appointment.Priority.SENIOR, "Blood pressure review");
        bookDemoAppointment(patients.get(3), generalDoctor, tomorrow.plusDays(1).atTime(11, 0),
                Appointment.Status.SCHEDULED, Appointment.Priority.NORMAL, "Annual wellness consultation");

        Appointment completed = bookDemoAppointment(patients.get(4), cardioDoctor,
                LocalDateTime.now().minusDays(2).withHour(10).withMinute(30).withSecond(0).withNano(0),
                Appointment.Status.COMPLETED, Appointment.Priority.NORMAL, "Chest discomfort consultation completed");

        seedPrescription(completed);
        seedBilling(completed);
    }

    private Appointment bookDemoAppointment(Patient patient, Doctor doctor, LocalDateTime slot,
            Appointment.Status status, Appointment.Priority priority, String notes) {
        Appointment appointment = new Appointment();
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setSlotDatetime(slot);
        appointment.setStatus(status);
        appointment.setPriority(priority);
        appointment.setNotes(notes);
        appointmentService.bookAppointment(appointment);
        return appointment;
    }

    private void seedPrescription(Appointment appointment) {
        if (!prescriptionService.getPatientPrescriptions(appointment.getPatient().getId()).isEmpty()) {
            return;
        }

        Prescription prescription = new Prescription();
        prescription.setAppointment(appointment);
        prescription.setDoctor(appointment.getDoctor());
        prescription.setPatient(appointment.getPatient());
        prescription.setDiagnosis("Stable angina symptoms reviewed. ECG advised, lipid profile requested, and medication started.");
        prescription.setClinicalTemplate("Cardiology Review");
        prescription.setDiagnosisTag("R07 Chest pain");
        prescription.setBloodPressure("128/84");
        prescription.setPulse("78");
        prescription.setTemperature("98.4 F");
        prescription.setSpo2("98%");
        prescription.setWeightKg("74");
        prescription.setLabOrders("ECG, Lipid profile");
        prescription.setFollowUpDays(14);
        prescription.setRiskFlags("Cardiac risk, follow-up required");
        prescription.setFavoriteName("Cardiac Starter Pack");

        PrescriptionItem aspirin = medicine("Aspirin", "75 mg", "30 days", 1, "Take once daily after breakfast");
        PrescriptionItem statin = medicine("Atorvastatin", "10 mg", "30 days", 1, "Take once at night");
        PrescriptionItem pantoprazole = medicine("Pantoprazole", "40 mg", "14 days", 1, "Take before breakfast");

        prescription.setItems(Arrays.asList(aspirin, statin, pantoprazole));
        prescription.getItems().forEach(item -> item.setPrescription(prescription));
        prescriptionService.createPrescription(prescription);
    }

    private PrescriptionItem medicine(String name, String dosage, String duration, int quantity, String instructions) {
        PrescriptionItem item = new PrescriptionItem();
        item.setMedicineName(name);
        item.setDosage(dosage);
        item.setDuration(duration);
        item.setQuantity(quantity);
        item.setInstructions(instructions);
        return item;
    }

    private void seedBilling(Appointment appointment) {
        if (!billingService.findAll().isEmpty()) {
            return;
        }

        Billing bill = billingService.generateBill(appointment.getId());
        if (bill != null && bill.getId() != null) {
            billingService.updatePayment(bill.getId(), "PAID", "UPI", bill.getTotal(), "DEMO-UPI-001",
                    BigDecimal.ZERO, null, "Demo Health Insurance", "CLM-1001", "APPROVED");
        }
    }

    private void seedOperationalDemoRecords() {
        if (reminderLogService.findAll().isEmpty()) {
            appointmentService.findAll().stream()
                    .filter(appointment -> appointment.getStatus() == Appointment.Status.SCHEDULED)
                    .findFirst()
                    .ifPresent(appointment -> reminderLogService.sendMockReminder(appointment.getId(), "SMS"));
        }

        if (stockMovementService.findAll().isEmpty()) {
            MedicineInventory atorvastatin = medicineInventoryService.findByName("Atorvastatin");
            MedicineInventory aspirin = medicineInventoryService.findByName("Aspirin");
            stockMovementService.record(atorvastatin, "RESTOCK", 20, "Opening reorder plan for low stock demo");
            stockMovementService.record(aspirin, "DISPENSE", 1, "Demo completed prescription #1");
        }
    }
}
