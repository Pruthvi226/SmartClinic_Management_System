package com.smartclinic.model;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "prescriptions")
public class Prescription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id", nullable = false)
    @NotNull(message = "Appointment is required for prescription")
    private Appointment appointment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    @NotNull(message = "Doctor is required for prescription")
    private Doctor doctor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    @NotNull(message = "Patient is required for prescription")
    private Patient patient;

    @Column(columnDefinition = "TEXT", nullable = false)
    @NotBlank(message = "Diagnosis is required")
    @Size(min = 5, max = 2000, message = "Diagnosis must be between 5 and 2000 characters")
    private String diagnosis;

    @Column(name = "clinical_template", length = 100)
    private String clinicalTemplate;

    @Column(name = "diagnosis_tag", length = 100)
    private String diagnosisTag;

    @Column(name = "blood_pressure", length = 30)
    private String bloodPressure;

    @Column(length = 30)
    private String pulse;

    @Column(length = 30)
    private String temperature;

    @Column(length = 30)
    private String spo2;

    @Column(name = "weight_kg", length = 30)
    private String weightKg;

    @Column(name = "lab_orders", columnDefinition = "TEXT")
    private String labOrders;

    @Column(name = "follow_up_days")
    private Integer followUpDays;

    @Column(name = "risk_flags", columnDefinition = "TEXT")
    private String riskFlags;

    @Column(name = "favorite_name", length = 100)
    private String favoriteName;

    @Column(nullable = false)
    private boolean draft = false;

    @Column(name = "issued_at", insertable = false, updatable = false)
    private Timestamp issuedAt;

    @Column(nullable = false)
    private boolean dispensed = false;

    @Column(name = "dispensed_at")
    private Timestamp dispensedAt;

    @OneToMany(mappedBy = "prescription", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<PrescriptionItem> items;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Appointment getAppointment() {
        return appointment;
    }

    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getClinicalTemplate() {
        return clinicalTemplate;
    }

    public void setClinicalTemplate(String clinicalTemplate) {
        this.clinicalTemplate = clinicalTemplate;
    }

    public String getDiagnosisTag() {
        return diagnosisTag;
    }

    public void setDiagnosisTag(String diagnosisTag) {
        this.diagnosisTag = diagnosisTag;
    }

    public String getBloodPressure() {
        return bloodPressure;
    }

    public void setBloodPressure(String bloodPressure) {
        this.bloodPressure = bloodPressure;
    }

    public String getPulse() {
        return pulse;
    }

    public void setPulse(String pulse) {
        this.pulse = pulse;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getSpo2() {
        return spo2;
    }

    public void setSpo2(String spo2) {
        this.spo2 = spo2;
    }

    public String getWeightKg() {
        return weightKg;
    }

    public void setWeightKg(String weightKg) {
        this.weightKg = weightKg;
    }

    public String getLabOrders() {
        return labOrders;
    }

    public void setLabOrders(String labOrders) {
        this.labOrders = labOrders;
    }

    public Integer getFollowUpDays() {
        return followUpDays;
    }

    public void setFollowUpDays(Integer followUpDays) {
        this.followUpDays = followUpDays;
    }

    public String getRiskFlags() {
        return riskFlags;
    }

    public void setRiskFlags(String riskFlags) {
        this.riskFlags = riskFlags;
    }

    public String getFavoriteName() {
        return favoriteName;
    }

    public void setFavoriteName(String favoriteName) {
        this.favoriteName = favoriteName;
    }

    public boolean isDraft() {
        return draft;
    }

    public void setDraft(boolean draft) {
        this.draft = draft;
    }

    public Timestamp getIssuedAt() {
        return issuedAt;
    }

    public void setIssuedAt(Timestamp issuedAt) {
        this.issuedAt = issuedAt;
    }

    public boolean isDispensed() {
        return dispensed;
    }

    public void setDispensed(boolean dispensed) {
        this.dispensed = dispensed;
    }

    public Timestamp getDispensedAt() {
        return dispensedAt;
    }

    public void setDispensedAt(Timestamp dispensedAt) {
        this.dispensedAt = dispensedAt;
    }

    public List<PrescriptionItem> getItems() {
        return items;
    }

    public void setItems(List<PrescriptionItem> items) {
        this.items = items;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Prescription))
            return false;
        Prescription that = (Prescription) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Prescription{" +
                "id=" + id +
                ", diagnosis='" + diagnosis + '\'' +
                '}';
    }
}
