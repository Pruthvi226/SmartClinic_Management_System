package com.smartclinic.model;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "appointments")
@Cacheable
@org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.READ_WRITE)
@NamedQueries({
        // Solving N+1 by using JOIN FETCH for doctor and patient fetching
        // simultaneously
        @NamedQuery(name = "Appointment.findUpcomingByDoctor", query = "SELECT a FROM Appointment a JOIN FETCH a.patient JOIN FETCH a.doctor d JOIN FETCH d.user WHERE a.doctor.id = :doctorId AND a.status = 'SCHEDULED' AND a.slotDatetime >= current_timestamp ORDER BY a.slotDatetime ASC")
})
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    @NotNull(message = "Patient is required for appointment")
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    @NotNull(message = "Doctor is required for appointment")
    private Doctor doctor;

    @Column(name = "slot_datetime", nullable = false)
    @NotNull(message = "Appointment date and time is required")
    private LocalDateTime slotDatetime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull(message = "Appointment status is required")
    private Status status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull(message = "Appointment priority is required")
    private Priority priority;

    @Column(columnDefinition = "TEXT")
    @Size(max = 1000, message = "Notes must not exceed 1000 characters")
    private String notes;

    @Column(name = "queue_state", length = 50)
    private String queueState = "WAITING";

    @Column(name = "token_number", length = 30)
    private String tokenNumber;

    public enum Status {
        SCHEDULED, COMPLETED, CANCELLED
    }

    public enum Priority {
        NORMAL, SENIOR, EMERGENCY
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public LocalDateTime getSlotDatetime() {
        return slotDatetime;
    }

    public void setSlotDatetime(LocalDateTime slotDatetime) {
        this.slotDatetime = slotDatetime;
    }

    public Timestamp getSlotDatetimeAsDate() {
        return slotDatetime == null ? null : Timestamp.valueOf(slotDatetime);
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getQueueState() {
        return queueState == null || queueState.trim().isEmpty() ? "WAITING" : queueState;
    }

    public void setQueueState(String queueState) {
        this.queueState = queueState;
    }

    public String getQueueStateLabel() {
        return getQueueState().replace('_', ' ');
    }

    public String getTokenNumber() {
        return tokenNumber == null || tokenNumber.trim().isEmpty() ? "Q" + (id == null ? "" : id) : tokenNumber;
    }

    public void setTokenNumber(String tokenNumber) {
        this.tokenNumber = tokenNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Appointment))
            return false;
        Appointment that = (Appointment) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Appointment{" +
                "id=" + id +
                ", slotDatetime=" + slotDatetime +
                ", status=" + status +
                ", priority=" + priority +
                '}';
    }
}
