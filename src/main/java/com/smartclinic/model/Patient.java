package com.smartclinic.model;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "patients")
@Cacheable
@org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.READ_WRITE)
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotBlank(message = "Patient name is required")
    @Size(min = 2, max = 255, message = "Patient name must be between 2 and 255 characters")
    private String name;

    @Column
    private Date dob;

    @Column
    @Min(value = 0, message = "Age must be a positive number")
    @Max(value = 150, message = "Age must be less than 150")
    private Integer age;

    @Column(length = 20)
    @Pattern(regexp = "^(MALE|FEMALE|OTHER)?$", message = "Gender must be MALE, FEMALE, or OTHER")
    private String gender;

    @Column(length = 20)
    @Pattern(regexp = "^[0-9\\-\\+\\s\\(\\)]*$", message = "Phone number format is invalid")
    private String phone;

    @Email(message = "Email address must be valid")
    private String email;

    @Column(name = "blood_group", length = 10)
    @Pattern(regexp = "^(O\\+|O\\-|A\\+|A\\-|B\\+|B\\-|AB\\+|AB\\-)?$", message = "Invalid blood group")
    private String bloodGroup;

    @Column(columnDefinition = "TEXT")
    @Size(max = 1000, message = "Address must not exceed 1000 characters")
    private String address;

    @Column(name = "registered_at", insertable = false, updatable = false)
    private Timestamp registeredAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Timestamp getRegisteredAt() {
        return registeredAt;
    }

    public void setRegisteredAt(Timestamp registeredAt) {
        this.registeredAt = registeredAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Patient))
            return false;
        Patient patient = (Patient) o;
        return Objects.equals(id, patient.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Patient{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
