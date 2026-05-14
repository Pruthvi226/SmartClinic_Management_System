# Smart Hospital Management System

A comprehensive Spring MVC-based Hospital Management System with patient management, doctor scheduling, appointment booking, prescription management, and billing features.

## 📋 Table of Contents

- [Features](#features)
- [Tech Stack](#tech-stack)
- [Prerequisites](#prerequisites)
- [Quick Start](#quick-start)
- [Local Development](#local-development)
- [Docker Deployment](#docker-deployment)
- [API Documentation](#api-documentation)
- [Database Schema](#database-schema)
- [Configuration](#configuration)
- [Troubleshooting](#troubleshooting)

---

## ✨ Features

### Patient Management

- Register new patients with complete health information
- View and update patient profiles
- Search patients by name or contact details
- Track patient appointment history
- Blood group and medical history records

### Doctor Management

- Add and manage doctor profiles
- Define specializations and availability
- Set appointment slot duration
- Manage working days and schedule
- View upcoming appointments

### Appointment Scheduling

- Book appointments with available time slots
- Check doctor availability by date and time
- Manage appointment priorities (Normal, Senior, Emergency)
- Update appointment status (Scheduled, Completed, Cancelled)
- View patient and doctor appointment queues

### Prescription Management

- Create prescriptions during appointments
- Manage prescription items with dosage information
- Track prescription history by patient
- Issue date tracking

### Billing & Payments

- Auto-generate bills for appointments
- Apply taxes and discounts
- Track payment status
- View billing history and summaries

### Audit & Security

- Complete audit logging for all operations
- Role-based access control (ADMIN, DOCTOR, RECEPTIONIST, PHARMACIST)
- Spring Security integration
- BCrypt password encryption
- Login/logout functionality

---

## 🛠 Tech Stack

- **Backend:** Spring MVC 5.3.27
- **ORM:** Hibernate 5.6.15
- **Database:** MySQL 8.0
- **Java:** JDK 17
- **Build Tool:** Maven 3.8.4
- **Security:** Spring Security 5.8.3
- **Validation:** Hibernate Validator
- **Caching:** EhCache
- **Deployment:** Tomcat 9.0 / Docker

---

## 📦 Prerequisites

### Local Development

- **Java 17+** ([Download](https://www.oracle.com/java/technologies/downloads/#java17))
- **Maven 3.8.4+** ([Download](https://maven.apache.org/download.cgi))
- **MySQL 8.0+** ([Download](https://dev.mysql.com/downloads/mysql/))
- **Git** ([Download](https://git-scm.com/))

### Docker Deployment

- **Docker** ([Download](https://www.docker.com/products/docker-desktop))
- **Docker Compose** (included with Docker Desktop)

---

## 🚀 Quick Start

### Using Docker (Recommended)

1. **Clone the repository**

   ```bash
   git clone <repository-url>
   cd Smart_Hospital_Management
   ```

2. **Create environment file**

   ```bash
   cp .env.example .env
   # Edit .env with your database credentials
   ```

3. **Build and run**

   ```bash
   docker-compose up --build
   ```

4. **Access the application**
   - Web UI: http://localhost:8080
   - Login with default credentials:
     - Email: `admin@smartclinic.com`
     - Password: `admin123`

---

## 💻 Local Development

### 1. Setup MySQL Database

```bash
# Create database
mysql -u root -p
> CREATE DATABASE IF NOT EXISTS smartclinic;
> USE smartclinic;

# Hibernate will auto-create tables on first run
```

### 2. Configure Database Connection

Edit `src/main/resources/database.properties`:

```properties
db.driver=com.mysql.cj.jdbc.Driver
db.url=jdbc:mysql://localhost:3306/smartclinic?useSSL=false&serverTimezone=UTC
db.username=root
db.password=your_password
```

Or set environment variables:

```bash
export DB_HOST=localhost
export DB_PORT=3306
export DB_NAME=smartclinic
export DB_USER=root
export DB_PASSWORD=your_password
```

### 3. Build Project

```bash
cd Smart_Hospital_Management
mvn clean install
```

### 4. Run Application

**Using Jetty (embedded):**

```bash
mvn jetty:run
```

**Using Tomcat (after building WAR):**

```bash
# WAR file location: target/smart-clinic-1.0-SNAPSHOT.war
# Deploy to Tomcat webapps folder
cp target/smart-clinic-1.0-SNAPSHOT.war $CATALINA_HOME/webapps/ROOT.war
```

### 5. Access Application

- **URL:** http://localhost:8080
- **Default Admin:** admin@smartclinic.com / admin123
- **Default Doctor:** doctor@smartclinic.com / doctor123

---

## 🐳 Docker Deployment

### Using Docker Compose

```bash
# Build and start services
docker-compose up --build

# Stop services
docker-compose down

# View logs
docker-compose logs -f app

# Access MySQL from container
docker exec -it smartclinic-db mysql -u root -p smartclinic
```

### Production Deployment

1. **Update .env file with production values:**

   ```bash
   DB_HOST=production-mysql-host
   DB_PASSWORD=secure_password_here
   SPRING_PROFILES_ACTIVE=prod
   ```

2. **Use production docker-compose:**

   ```bash
   docker-compose -f docker-compose.yml up -d
   ```

3. **Verify services:**
   ```bash
   docker-compose ps
   docker logs smartclinic-app
   ```

---

## 📚 API Documentation

### Base URL

```
http://localhost:8080/api
```

### Authentication

All endpoints require Spring Security authentication. Login first to get session cookies.

### Patient API

#### Get all patients

```
GET /api/patients
Response: { status: "success", count: 5, data: [...] }
```

#### Get patient by ID

```
GET /api/patients/{id}
Response: { status: "success", data: {...} }
```

#### Search patients

```
GET /api/patients/search?keyword=John
Response: { status: "success", count: 2, data: [...] }
```

#### Register patient

```
POST /api/patients
Content-Type: application/json

{
  "name": "John Doe",
  "dob": "1990-05-15",
  "gender": "MALE",
  "phone": "9876543210",
  "email": "john@example.com",
  "bloodGroup": "O+",
  "address": "123 Main St, City"
}

Response: { status: "success", message: "Patient registered successfully", data: {...} }
```

#### Update patient

```
PUT /api/patients/{id}
Content-Type: application/json

{ "name": "Updated Name", ... }

Response: { status: "success", message: "Patient updated successfully", data: {...} }
```

---

### Doctor API

#### Get all doctors

```
GET /api/doctors
Response: { status: "success", count: 3, data: [...] }
```

#### Get doctor by ID

```
GET /api/doctors/{id}
Response: { status: "success", data: {...} }
```

#### Create doctor

```
POST /api/doctors
Content-Type: application/json

{
  "user": { "id": 2 },
  "specialization": "Cardiology",
  "availableDays": "MONDAY,TUESDAY,WEDNESDAY,THURSDAY,FRIDAY",
  "slotDurationMins": 30
}

Response: { status: "success", message: "Doctor created successfully", data: {...} }
```

#### Update doctor

```
PUT /api/doctors/{id}
Content-Type: application/json

{ "specialization": "Updated Specialty", ... }

Response: { status: "success", message: "Doctor updated successfully", data: {...} }
```

---

### Appointment API

#### Get available slots

```
GET /api/appointments/slots?doctorId=1&date=2026-05-20&priority=NORMAL
Response: { status: "success", count: 8, data: ["09:00", "09:30", ...] }
```

#### Get doctor queue

```
GET /api/appointments/queue/{doctorId}
Response: { status: "success", count: 5, data: [...] }
```

#### Get patient appointments

```
GET /api/appointments/patient/{patientId}
Response: { status: "success", count: 3, data: [...] }
```

#### Book appointment

```
POST /api/appointments
Content-Type: application/json

{
  "patient": { "id": 1 },
  "doctor": { "id": 1 },
  "slotDatetime": "2026-05-20T09:00:00",
  "status": "SCHEDULED",
  "priority": "NORMAL",
  "notes": "Regular checkup"
}

Response: { status: "success", message: "Appointment booked successfully", data: {...} }
```

#### Update appointment status

```
PUT /api/appointments/{id}/status?status=COMPLETED
Response: { status: "success", message: "Appointment status updated successfully", data: {...} }
```

---

### Billing API

#### Get all billing records

```
GET /api/billing
Response: { status: "success", count: 10, data: [...] }
```

#### Get bill by ID

```
GET /api/billing/{id}
Response: { status: "success", data: {...} }
```

#### Generate bill for appointment

```
POST /api/billing/generate?appointmentId=5
Response: { status: "success", message: "Billing record generated successfully", data: {...} }
```

#### Update payment status

```
PUT /api/billing/{id}/payment-status?status=COMPLETED
Response: { status: "success", message: "Payment status updated successfully", data: {...} }
```

#### Get billing summary

```
GET /api/billing/summary
Response: {
  status: "success",
  totalBillingRecords: 10,
  totalAmount: 50000,
  paidAmount: 30000,
  pendingAmount: 20000
}
```

---

### Prescription API

#### Get all prescriptions

```
GET /api/prescriptions
Response: { status: "success", count: 8, data: [...] }
```

#### Get prescription by ID

```
GET /api/prescriptions/{id}
Response: { status: "success", data: {...} }
```

#### Get patient prescriptions

```
GET /api/prescriptions/patient/{patientId}
Response: { status: "success", count: 3, data: [...] }
```

#### Create prescription

```
POST /api/prescriptions
Content-Type: application/json

{
  "appointment": { "id": 5 },
  "doctor": { "id": 1 },
  "patient": { "id": 1 },
  "diagnosis": "Common flu with mild fever",
  "items": [
    {
      "medicineName": "Aspirin",
      "dosage": "500mg",
      "duration": "5 days",
      "instructions": "Take twice daily after meals"
    }
  ]
}

Response: { status: "success", message: "Prescription created successfully", data: {...} }
```

---

## 🗄 Database Schema

### Tables

| Table                | Purpose                                                            |
| -------------------- | ------------------------------------------------------------------ |
| `users`              | User accounts with roles (ADMIN, DOCTOR, RECEPTIONIST, PHARMACIST) |
| `patients`           | Patient information and demographics                               |
| `doctors`            | Doctor specialization and availability                             |
| `appointments`       | Appointment records with status and priority                       |
| `prescriptions`      | Prescription records                                               |
| `prescription_items` | Individual medicine items in prescriptions                         |
| `billing`            | Billing records and payment status                                 |
| `audit_log`          | Audit trail for all user actions                                   |

### Entity Relationships

- **User → Doctor** (One-to-One)
- **Patient → Appointment** (One-to-Many)
- **Doctor → Appointment** (One-to-Many)
- **Doctor → Prescription** (One-to-Many)
- **Patient → Prescription** (One-to-Many)
- **Appointment → Prescription** (One-to-One)
- **Appointment → Billing** (One-to-One)
- **User → Audit Log** (One-to-Many)

---

## ⚙️ Configuration

### Environment Variables

Create `.env` file in project root:

```bash
# Database
DB_HOST=localhost
DB_PORT=3306
DB_NAME=smartclinic
DB_USER=root
DB_PASSWORD=secure_password

# Application
SPRING_PROFILES_ACTIVE=prod
APP_PORT=8080
DB_CONTAINER_PORT=3306
```

### Property Files

- **Development:** `src/main/resources/database.properties`
- **Production:** `src/main/resources/application-prod.properties`

### Spring Profiles

**Activate profile:**

```bash
export SPRING_PROFILES_ACTIVE=prod
mvn spring-boot:run
```

---

## 🐛 Troubleshooting

### Database Connection Issues

**Error: "Connection refused"**

- Ensure MySQL is running
- Check DB_HOST, DB_PORT, DB_USER, DB_PASSWORD in `.env` or properties file
- Verify database exists: `mysql -u root -p -e "SHOW DATABASES;"`

### Build Failures

**Error: "Maven build failed"**

```bash
# Clean and rebuild
mvn clean install -DskipTests

# Check Java version
java -version  # Should be 17+

# Verify Maven
mvn -version
```

### Docker Issues

**Error: "Port already in use"**

```bash
# Change port in .env or docker-compose.yml
APP_PORT=8081
```

**Error: "Database connection timeout in Docker"**

```bash
# Check MySQL logs
docker logs smartclinic-db

# Verify network
docker network ls
docker inspect smart_hospital_management_default
```

### Application Not Starting

**Check logs:**

```bash
# Local
mvn jetty:run 2>&1 | tail -50

# Docker
docker logs smartclinic-app

# Check port
netstat -tuln | grep 8080
```

---

## 📝 Default Users

Created automatically on first run:

| Email                  | Password  | Role   |
| ---------------------- | --------- | ------ |
| admin@smartclinic.com  | admin123  | ADMIN  |
| doctor@smartclinic.com | doctor123 | DOCTOR |

⚠️ **Change passwords immediately in production!**

---

## 🔐 Security Notes

1. **Database Passwords:** Use strong, randomly generated passwords
2. **HTTPS:** Enable SSL/TLS in production
3. **Environment Variables:** Never commit `.env` with real credentials
4. **Session Cookies:** Configured with HttpOnly and Secure flags
5. **SQL Injection:** Protected via Hibernate ORM
6. **CSRF:** Spring Security CSRF protection enabled
7. **Password Encryption:** BCrypt hashing (cost factor 10)

---

## 📖 Additional Resources

- [Spring Framework Docs](https://spring.io/projects/spring-framework)
- [Hibernate Documentation](https://hibernate.org/orm/documentation/)
- [MySQL Documentation](https://dev.mysql.com/doc/)
- [Docker Documentation](https://docs.docker.com/)

---

## 📄 License

This project is licensed under the MIT License - see LICENSE file for details.

---

## 👥 Support

For issues, questions, or suggestions:

1. Check the [Troubleshooting](#troubleshooting) section
2. Review logs for error details
3. Create an issue in the repository

---

**Last Updated:** May 2026
**Version:** 1.0.0
