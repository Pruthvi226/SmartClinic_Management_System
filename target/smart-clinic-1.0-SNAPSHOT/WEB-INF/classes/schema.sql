-- SmartClinic MySQL bootstrap for Docker demos.
-- Hibernate also runs with hbm2ddl.auto=update, so these definitions are kept
-- compatible with the JPA entities and provide deterministic seed data.

CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role ENUM('ADMIN', 'DOCTOR', 'RECEPTIONIST', 'PHARMACIST') NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS patients (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    dob DATE NULL,
    age INT NULL,
    gender VARCHAR(20),
    phone VARCHAR(20),
    email VARCHAR(255),
    blood_group VARCHAR(10),
    address TEXT,
    allergies TEXT,
    registered_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_patient_phone (phone),
    INDEX idx_patient_name (name)
);

CREATE TABLE IF NOT EXISTS doctors (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL UNIQUE,
    specialization VARCHAR(255) NOT NULL,
    available_days VARCHAR(255) NOT NULL,
    slot_duration_mins INT NOT NULL DEFAULT 30,
    CONSTRAINT fk_doctors_user FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS doctor_leaves (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    doctor_id BIGINT NOT NULL,
    leave_date DATE NOT NULL,
    reason VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_doctor_leaves_doctor FOREIGN KEY (doctor_id) REFERENCES doctors(id),
    CONSTRAINT uk_doctor_leave_date UNIQUE (doctor_id, leave_date),
    INDEX idx_doctor_leaves_date (doctor_id, leave_date)
);

CREATE TABLE IF NOT EXISTS appointments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    patient_id BIGINT NOT NULL,
    doctor_id BIGINT NOT NULL,
    slot_datetime DATETIME NOT NULL,
    status ENUM('SCHEDULED', 'COMPLETED', 'CANCELLED') NOT NULL DEFAULT 'SCHEDULED',
    priority ENUM('NORMAL', 'SENIOR', 'EMERGENCY') NOT NULL DEFAULT 'NORMAL',
    notes TEXT,
    CONSTRAINT fk_appointments_patient FOREIGN KEY (patient_id) REFERENCES patients(id),
    CONSTRAINT fk_appointments_doctor FOREIGN KEY (doctor_id) REFERENCES doctors(id),
    INDEX idx_appointment_doctor_date (doctor_id, slot_datetime),
    INDEX idx_appointment_patient (patient_id)
);

CREATE TABLE IF NOT EXISTS appointment_waitlist (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    patient_id BIGINT NOT NULL,
    doctor_id BIGINT NOT NULL,
    requested_date DATE NOT NULL,
    priority ENUM('NORMAL', 'SENIOR', 'EMERGENCY') NOT NULL DEFAULT 'NORMAL',
    status ENUM('WAITING', 'NOTIFIED', 'BOOKED', 'CANCELLED') NOT NULL DEFAULT 'WAITING',
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_waitlist_patient FOREIGN KEY (patient_id) REFERENCES patients(id),
    CONSTRAINT fk_waitlist_doctor FOREIGN KEY (doctor_id) REFERENCES doctors(id),
    INDEX idx_waitlist_doctor_date_status (doctor_id, requested_date, status),
    INDEX idx_waitlist_status (status)
);

CREATE TABLE IF NOT EXISTS prescriptions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    appointment_id BIGINT NOT NULL UNIQUE,
    doctor_id BIGINT NOT NULL,
    patient_id BIGINT NOT NULL,
    diagnosis TEXT NOT NULL,
    issued_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    dispensed BOOLEAN NOT NULL DEFAULT FALSE,
    dispensed_at TIMESTAMP NULL,
    CONSTRAINT fk_prescriptions_appointment FOREIGN KEY (appointment_id) REFERENCES appointments(id),
    CONSTRAINT fk_prescriptions_doctor FOREIGN KEY (doctor_id) REFERENCES doctors(id),
    CONSTRAINT fk_prescriptions_patient FOREIGN KEY (patient_id) REFERENCES patients(id)
);

CREATE TABLE IF NOT EXISTS prescription_items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    prescription_id BIGINT NOT NULL,
    medicine_name VARCHAR(255) NOT NULL,
    dosage VARCHAR(255) NOT NULL,
    duration VARCHAR(255) NOT NULL,
    quantity INT DEFAULT 1,
    instructions TEXT,
    CONSTRAINT fk_prescription_items_prescription FOREIGN KEY (prescription_id) REFERENCES prescriptions(id)
);

CREATE TABLE IF NOT EXISTS medicine_inventory (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    medicine_name VARCHAR(255) NOT NULL UNIQUE,
    category VARCHAR(100),
    stock_quantity INT NOT NULL DEFAULT 0,
    reorder_level INT NOT NULL DEFAULT 10,
    unit_price DECIMAL(10,2) DEFAULT 0.00,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_medicine_inventory_name (medicine_name),
    INDEX idx_medicine_inventory_low_stock (stock_quantity, reorder_level)
);

CREATE TABLE IF NOT EXISTS billing (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    appointment_id BIGINT NOT NULL UNIQUE,
    amount DECIMAL(10,2) NOT NULL,
    tax DECIMAL(10,2) NOT NULL,
    total DECIMAL(10,2) NOT NULL,
    payment_status VARCHAR(50) DEFAULT 'PENDING',
    payment_mode VARCHAR(50) DEFAULT 'UNPAID',
    paid_amount DECIMAL(10,2) DEFAULT 0.00,
    payment_reference VARCHAR(100),
    generated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_billing_appointment FOREIGN KEY (appointment_id) REFERENCES appointments(id)
);

CREATE TABLE IF NOT EXISTS audit_log (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT,
    action VARCHAR(255) NOT NULL,
    entity_type VARCHAR(100),
    entity_id BIGINT,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ip_address VARCHAR(45),
    CONSTRAINT fk_audit_log_user FOREIGN KEY (user_id) REFERENCES users(id)
);

DROP PROCEDURE IF EXISTS calculate_tax;
DELIMITER //
CREATE PROCEDURE calculate_tax(IN base_amount DECIMAL(10,2), OUT calculated_tax DECIMAL(10,2))
BEGIN
    SET calculated_tax = ROUND(base_amount * 0.18, 2);
END //
DELIMITER ;

INSERT INTO users (id, name, email, password, role) VALUES
(1, 'System Admin', 'admin@smartclinic.com', '$2a$10$UwEnrTGxSsw47mCslpYQ.uqzLxdv8eeLGt2pZac03AYUJreiG6jmO', 'ADMIN'),
(2, 'Dr. John Smith', 'doctor@smartclinic.com', '$2a$10$/2KEMkNnaEyLZn0w2WjkGuDJBneQ6vhFv1/BdjU3vROD6POnn3Q/W', 'DOCTOR'),
(3, 'Reception Desk', 'reception@smartclinic.com', '$2a$10$lZ8uy9VoIdb20c2NpRD4lep6Ge42xNYzH6.oElwYBdvS/9L3Z0DpG', 'RECEPTIONIST'),
(4, 'Pharmacy Desk', 'pharmacy@smartclinic.com', '$2a$10$GXMyrRQ4Ps/Qf9OCvyXOYukb1T0G9mTiB1lY8b75yCTihU5c7GZEm', 'PHARMACIST'),
(5, 'Dr. Priya Nair', 'cardio@smartclinic.com', '$2a$10$/2KEMkNnaEyLZn0w2WjkGuDJBneQ6vhFv1/BdjU3vROD6POnn3Q/W', 'DOCTOR')
ON DUPLICATE KEY UPDATE
    name = VALUES(name),
    password = VALUES(password),
    role = VALUES(role);

INSERT INTO doctors (id, user_id, specialization, available_days, slot_duration_mins) VALUES
(1, 2, 'General Physician', 'MONDAY,TUESDAY,WEDNESDAY,THURSDAY,FRIDAY', 30),
(2, 5, 'Cardiology', 'MONDAY,WEDNESDAY,FRIDAY', 30)
ON DUPLICATE KEY UPDATE
    specialization = VALUES(specialization),
    available_days = VALUES(available_days),
    slot_duration_mins = VALUES(slot_duration_mins);

INSERT INTO patients (id, name, dob, age, gender, phone, email, blood_group, address, allergies) VALUES
(1, 'Aarav Mehta', '1992-08-12', 33, 'MALE', '+91 98765 43210', 'aarav.mehta@example.com', 'O+', 'Bandra West, Mumbai', 'Penicillin'),
(2, 'Neha Sharma', '1988-03-24', 38, 'FEMALE', '+91 99887 77665', 'neha.sharma@example.com', 'A+', 'Indiranagar, Bengaluru', 'Aspirin'),
(3, 'Rohan Iyer', '1956-11-05', 69, 'MALE', '+91 91234 56789', 'rohan.iyer@example.com', 'B+', 'Adyar, Chennai', 'None'),
(4, 'Fatima Khan', '2001-01-17', 25, 'FEMALE', '+91 90123 45678', 'fatima.khan@example.com', 'AB+', 'Koregaon Park, Pune', 'Sulfa'),
(5, 'Vikram Rao', '1978-07-30', 47, 'MALE', '+91 93456 78120', 'vikram.rao@example.com', 'O-', 'Jubilee Hills, Hyderabad', 'None')
ON DUPLICATE KEY UPDATE
    name = VALUES(name),
    phone = VALUES(phone),
    email = VALUES(email),
    blood_group = VALUES(blood_group),
    address = VALUES(address),
    allergies = VALUES(allergies);

INSERT INTO appointments (id, patient_id, doctor_id, slot_datetime, status, priority, notes) VALUES
(1, 1, 1, TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL 1 DAY), '09:00:00'), 'SCHEDULED', 'NORMAL', 'Fever and body ache follow-up'),
(2, 2, 1, TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL 1 DAY), '09:30:00'), 'SCHEDULED', 'EMERGENCY', 'Shortness of breath triage'),
(3, 3, 2, TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL 1 DAY), '10:00:00'), 'SCHEDULED', 'SENIOR', 'Blood pressure review'),
(4, 4, 1, TIMESTAMP(DATE_ADD(CURDATE(), INTERVAL 2 DAY), '11:00:00'), 'SCHEDULED', 'NORMAL', 'Annual wellness consultation'),
(5, 5, 2, TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 2 DAY), '10:30:00'), 'COMPLETED', 'NORMAL', 'Chest discomfort consultation completed')
ON DUPLICATE KEY UPDATE
    patient_id = VALUES(patient_id),
    doctor_id = VALUES(doctor_id),
    slot_datetime = VALUES(slot_datetime),
    status = VALUES(status),
    priority = VALUES(priority),
    notes = VALUES(notes);

INSERT INTO prescriptions (id, appointment_id, doctor_id, patient_id, diagnosis, issued_at, dispensed) VALUES
(1, 5, 2, 5, 'Stable angina symptoms reviewed. ECG advised, lipid profile requested, and medication started.', DATE_SUB(NOW(), INTERVAL 2 DAY), FALSE)
ON DUPLICATE KEY UPDATE
    diagnosis = VALUES(diagnosis),
    issued_at = VALUES(issued_at),
    dispensed = VALUES(dispensed);

INSERT INTO prescription_items (id, prescription_id, medicine_name, dosage, duration, quantity, instructions) VALUES
(1, 1, 'Aspirin', '75 mg', '30 days', 1, 'Take once daily after breakfast'),
(2, 1, 'Atorvastatin', '10 mg', '30 days', 1, 'Take once at night'),
(3, 1, 'Pantoprazole', '40 mg', '14 days', 1, 'Take before breakfast')
ON DUPLICATE KEY UPDATE
    medicine_name = VALUES(medicine_name),
    dosage = VALUES(dosage),
    duration = VALUES(duration),
    quantity = VALUES(quantity),
    instructions = VALUES(instructions);

INSERT INTO medicine_inventory (id, medicine_name, category, stock_quantity, reorder_level, unit_price) VALUES
(1, 'Aspirin', 'Cardiology', 120, 20, 2.00),
(2, 'Atorvastatin', 'Cardiology', 8, 10, 12.00),
(3, 'Pantoprazole', 'Gastro', 45, 15, 6.00),
(4, 'Paracetamol', 'General', 150, 25, 3.00),
(5, 'Amoxicillin', 'Antibiotic', 25, 10, 8.00)
ON DUPLICATE KEY UPDATE
    category = VALUES(category),
    stock_quantity = VALUES(stock_quantity),
    reorder_level = VALUES(reorder_level),
    unit_price = VALUES(unit_price);

INSERT INTO billing (id, appointment_id, amount, tax, total, payment_status, payment_mode, paid_amount, payment_reference, generated_at) VALUES
(1, 5, 100.00, 18.00, 118.00, 'PAID', 'UPI', 118.00, 'DEMO-UPI-001', DATE_SUB(NOW(), INTERVAL 2 DAY))
ON DUPLICATE KEY UPDATE
    amount = VALUES(amount),
    tax = VALUES(tax),
    total = VALUES(total),
    payment_status = VALUES(payment_status),
    payment_mode = VALUES(payment_mode),
    paid_amount = VALUES(paid_amount),
    payment_reference = VALUES(payment_reference);

INSERT INTO audit_log (id, user_id, action, entity_type, entity_id, ip_address) VALUES
(1, 1, 'DEMO_SEED admin dashboard prepared', 'SYSTEM', 1, '127.0.0.1'),
(2, 3, 'DEMO_SEED patient registration workflow prepared', 'PATIENT', 1, '127.0.0.1'),
(3, 2, 'DEMO_SEED consultation workflow prepared', 'APPOINTMENT', 5, '127.0.0.1')
ON DUPLICATE KEY UPDATE
    action = VALUES(action),
    entity_type = VALUES(entity_type),
    entity_id = VALUES(entity_id),
    ip_address = VALUES(ip_address);

ALTER TABLE users AUTO_INCREMENT = 20;
ALTER TABLE doctors AUTO_INCREMENT = 20;
ALTER TABLE doctor_leaves AUTO_INCREMENT = 20;
ALTER TABLE patients AUTO_INCREMENT = 20;
ALTER TABLE appointments AUTO_INCREMENT = 20;
ALTER TABLE appointment_waitlist AUTO_INCREMENT = 20;
ALTER TABLE prescriptions AUTO_INCREMENT = 20;
ALTER TABLE prescription_items AUTO_INCREMENT = 20;
ALTER TABLE medicine_inventory AUTO_INCREMENT = 20;
ALTER TABLE billing AUTO_INCREMENT = 20;
ALTER TABLE audit_log AUTO_INCREMENT = 20;
