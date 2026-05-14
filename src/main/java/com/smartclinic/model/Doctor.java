package com.smartclinic.model;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.Objects;

@Entity
@Table(name = "doctors")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    @NotNull(message = "User is required for doctor record")
    private User user;

    @Column(nullable = false)
    @NotBlank(message = "Specialization is required")
    @Size(min = 2, max = 255, message = "Specialization must be between 2 and 255 characters")
    private String specialization;

    @Column(name = "available_days", nullable = false)
    @NotBlank(message = "Available days are required (comma-separated)")
    private String availableDays;

    @Column(name = "slot_duration_mins", nullable = false)
    @NotNull(message = "Slot duration in minutes is required")
    @Min(value = 15, message = "Slot duration must be at least 15 minutes")
    @Max(value = 480, message = "Slot duration must not exceed 480 minutes")
    private Integer slotDurationMins;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getAvailableDays() {
        return availableDays;
    }

    public void setAvailableDays(String availableDays) {
        this.availableDays = availableDays;
    }

    public Integer getSlotDurationMins() {
        return slotDurationMins;
    }

    public void setSlotDurationMins(Integer slotDurationMins) {
        this.slotDurationMins = slotDurationMins;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Doctor))
            return false;
        Doctor doctor = (Doctor) o;
        return Objects.equals(id, doctor.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Doctor{" +
                "id=" + id +
                ", specialization='" + specialization + '\'' +
                ", availableDays='" + availableDays + '\'' +
                '}';
    }
}
