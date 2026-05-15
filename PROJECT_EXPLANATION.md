# SmartClinic Management System - Project Explanation

## 1. Project Overview

SmartClinic Management System is a Java/J2EE web application for managing a small hospital or clinic. It supports role-based workflows for:

- Admin
- Doctor
- Receptionist
- Pharmacist

The project is built as a Maven WAR application and runs on Apache Tomcat. It uses Spring MVC for request handling, JSP/JSTL for server-rendered UI, Hibernate ORM for database persistence, MySQL for data storage, and Spring Security for login and role-based access.

The application is also Dockerized so the full system can be started with one command:

```bash
docker compose up --build -d
```

Demo URL:

```text
http://localhost:8080/login
```

## 2. High-Level Architecture

The project follows a layered MVC architecture:

```text
Browser
  |
  | HTTP request
  v
Spring Security Filter Chain
  |
  v
Spring MVC Controller
  |
  v
Service Layer
  |
  v
DAO Layer
  |
  v
Hibernate SessionFactory
  |
  v
MySQL Database
```

The layers are separated clearly:

| Layer | Purpose | Main package/files |
| --- | --- | --- |
| View layer | JSP pages rendered for users | `src/main/webapp/WEB-INF/views` |
| Controller layer | Handles web requests and routes users | `src/main/java/com/smartclinic/controller` |
| Service layer | Business logic and transaction boundary | `src/main/java/com/smartclinic/service` |
| DAO layer | Database access through Hibernate | `src/main/java/com/smartclinic/dao` |
| Model layer | JPA/Hibernate entity classes | `src/main/java/com/smartclinic/model` |
| Configuration | Spring, MVC, Security, Hibernate setup | `src/main/java/com/smartclinic/config` |
| Database seed | MySQL schema and demo data | `src/main/resources/schema.sql` |
| Deployment | Docker/Tomcat/MySQL setup | `Dockerfile`, `docker-compose.yml` |

## 3. Where Java Is Used

Java is the main programming language of the project.

### Java version

Configured in `pom.xml`:

```xml
<java.version>17</java.version>
<maven.compiler.source>17</maven.compiler.source>
<maven.compiler.target>17</maven.compiler.target>
```

### Java is used for:

- Spring MVC controllers
- Service classes
- DAO classes
- Hibernate entity classes
- Security configuration
- Utility classes
- Unit tests

Important Java packages:

```text
src/main/java/com/smartclinic/controller
src/main/java/com/smartclinic/service
src/main/java/com/smartclinic/dao
src/main/java/com/smartclinic/model
src/main/java/com/smartclinic/config
src/main/java/com/smartclinic/util
```

Example:

`AppointmentController.java` handles appointment booking, queue, reminders, calendar, waitlist, reschedule, cancellation, and appointment timeline routes.

`AppointmentServiceImpl.java` contains business logic for appointment booking, slot allocation, status updates, and queue behavior.

`Appointment.java` is a Java entity class mapped to the `appointments` table.

## 4. Where Spring MVC Is Used

Spring MVC is used to build the web layer.

### MVC configuration

File:

```text
src/main/java/com/smartclinic/config/WebConfig.java
```

This class:

- Enables Spring MVC using `@EnableWebMvc`
- Scans controller classes using `@ComponentScan`
- Configures JSP view resolution
- Serves static files from `/resources/**`
- Configures JSON conversion for REST APIs

Important code concept:

```java
resolver.setPrefix("/WEB-INF/views/");
resolver.setSuffix(".jsp");
```

This means when a controller returns:

```java
return "doctor/schedule";
```

Spring MVC renders:

```text
src/main/webapp/WEB-INF/views/doctor/schedule.jsp
```

### Controllers

Spring MVC controllers are in:

```text
src/main/java/com/smartclinic/controller
```

Examples:

| Controller | Responsibility |
| --- | --- |
| `AuthController.java` | Login page, root redirect, dashboard routing |
| `AdminController.java` | Admin dashboard, users, doctors, departments, settings, reports, audit logs |
| `AppointmentController.java` | Booking, queue, waitlist, calendar, reminders, reschedule, cancel |
| `DoctorController.java` | Doctor schedule, consultation, profile, completed consultations |
| `PatientController.java` | Patient registration, search, edit, history |
| `PharmacyController.java` | Prescription queue, inventory, restock, dispense, purchase order |
| `BillingController.java` | Billing list, invoice, payment, refund, PDF receipt |

### REST API controllers

The project also has REST-style controllers:

```text
AppointmentApiController.java
BillingApiController.java
DoctorApiController.java
PatientApiController.java
PrescriptionApiController.java
```

These use `@RestController` and return JSON responses using Jackson.

Example use:

```text
GET /api/appointments/slots?doctorId=1&date=2026-05-20&priority=NORMAL
```

This demonstrates Spring MVC request mapping, parameter binding, JSON serialization, and controller-service interaction.

## 5. Where JSP and JSTL Are Used

JSP is used for the user interface.

All JSP pages are under:

```text
src/main/webapp/WEB-INF/views
```

Important JSP folders:

| Folder | Purpose |
| --- | --- |
| `admin` | Admin dashboard, doctors, users, reports, settings, audit logs |
| `appointments` | Booking, queue, calendar, waitlist, reminders, timeline |
| `billing` | Billing list and invoice pages |
| `doctor` | Schedule, profile, consultation, completed consultations |
| `patients` | Register, search, history |
| `pharmacy` | Queue, inventory, movement history, purchase order |
| `layout` | Shared header and footer |
| `error` | 403 and generic error pages |

### JSP layout

Shared layout files:

```text
src/main/webapp/WEB-INF/views/layout/header.jsp
src/main/webapp/WEB-INF/views/layout/footer.jsp
```

The header contains:

- Navigation bar
- Role-based menu links using Spring Security tags
- CSS and JavaScript includes
- CSRF meta tags
- Automatic CSRF hidden input injection for POST forms

### JSTL usage

JSP pages use JSTL tags such as:

```jsp
<c:if>
<c:forEach>
<c:url>
```

This is used for:

- Showing success/error messages
- Looping through patients, appointments, billing records, doctors, medicines
- Creating context-aware URLs
- Conditionally displaying data

### Spring Security JSP tags

The project uses Spring Security taglibs in JSP:

```jsp
<sec:authorize access="hasAuthority('ADMIN')">
```

This allows the UI to show links based on logged-in user role.

## 6. Where MySQL Is Used

MySQL is the relational database used by the system.

### Docker MySQL service

Configured in:

```text
docker-compose.yml
```

Important settings:

```yaml
db:
  image: mysql:8.0
  ports:
    - "${DB_HOST_PORT:-3308}:3306"
```

This exposes MySQL on:

```text
localhost:3308
```

inside Docker, the app connects to:

```text
db:3306
```

### Schema and seed data

File:

```text
src/main/resources/schema.sql
```

This file creates and seeds tables such as:

- `users`
- `patients`
- `doctors`
- `doctor_leaves`
- `appointments`
- `appointment_waitlist`
- `prescriptions`
- `prescription_items`
- `medicine_inventory`
- `billing`
- `audit_log`
- `reminder_logs`
- `stock_movements`
- `departments`
- `system_settings`

### Demo users in MySQL

The default users are inserted in `schema.sql`.

| Role | Email | Password |
| --- | --- | --- |
| Admin | `admin@smartclinic.com` | `admin123` |
| Doctor | `doctor@smartclinic.com` | `doctor123` |
| Receptionist | `reception@smartclinic.com` | `reception123` |
| Pharmacist | `pharmacy@smartclinic.com` | `pharmacy123` |

The stored passwords are BCrypt hashes, not plain text.

### MySQL stored procedure

The schema includes a stored procedure:

```sql
calculate_tax
```

This is used to demonstrate database-side logic for billing/tax calculation.

## 7. Where Hibernate ORM Is Used

Hibernate ORM is used to map Java objects to MySQL tables.

### Hibernate configuration

File:

```text
src/main/java/com/smartclinic/config/AppConfig.java
```

Important Hibernate setup:

```java
LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
sessionFactory.setDataSource(dataSource());
sessionFactory.setPackagesToScan("com.smartclinic.model");
sessionFactory.setHibernateProperties(hibernateProperties());
```

This tells Hibernate:

- Use the configured MySQL datasource
- Scan `com.smartclinic.model` for entity classes
- Apply Hibernate properties from `database.properties`

### Transaction management

Also in `AppConfig.java`:

```java
HibernateTransactionManager
```

This manages database transactions for service methods.

### Hibernate properties

Configured through:

```text
src/main/resources/database.properties
src/main/resources/application-prod.properties
```

Hibernate settings include:

- SQL dialect
- SQL logging
- Schema update behavior
- Second-level cache
- Query cache

### Entity classes

Entity classes are in:

```text
src/main/java/com/smartclinic/model
```

Examples:

| Entity | Database table |
| --- | --- |
| `User.java` | `users` |
| `Patient.java` | `patients` |
| `Doctor.java` | `doctors` |
| `Appointment.java` | `appointments` |
| `AppointmentWaitlist.java` | `appointment_waitlist` |
| `Prescription.java` | `prescriptions` |
| `PrescriptionItem.java` | `prescription_items` |
| `MedicineInventory.java` | `medicine_inventory` |
| `Billing.java` | `billing` |
| `AuditLog.java` | `audit_log` |

### ORM relationships

The project demonstrates common Hibernate relationships:

| Relationship | Example |
| --- | --- |
| One-to-one | `User` to `Doctor` |
| Many-to-one | `Appointment` to `Patient` |
| Many-to-one | `Appointment` to `Doctor` |
| One-to-one | `Appointment` to `Billing` |
| One-to-one | `Appointment` to `Prescription` |
| One-to-many | `Prescription` to `PrescriptionItem` |
| One-to-many | `Doctor` to `DoctorLeave` |

Example from `Appointment.java`:

```java
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "patient_id", nullable = false)
private Patient patient;

@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "doctor_id", nullable = false)
private Doctor doctor;
```

This means one appointment belongs to one patient and one doctor, while one patient or doctor can have many appointments.

### DAO layer

DAO classes are in:

```text
src/main/java/com/smartclinic/dao
```

The common Hibernate DAO base class is:

```text
GenericDaoImpl.java
```

It uses:

```java
SessionFactory
getCurrentSession()
saveOrUpdate()
createQuery()
```

This proves that database operations are handled through Hibernate sessions rather than raw JDBC for normal application workflows.

## 8. Where J2EE Concepts Are Used

The project uses J2EE web application concepts through servlet-based deployment.

### WAR packaging

Configured in `pom.xml`:

```xml
<packaging>war</packaging>
```

The build generates:

```text
target/smart-clinic.war
```

### Servlet initializer

File:

```text
src/main/java/com/smartclinic/config/WebAppInitializer.java
```

This class replaces the old XML `web.xml` style configuration.

It extends:

```java
AbstractAnnotationConfigDispatcherServletInitializer
```

This sets up:

- Root Spring context
- DispatcherServlet context
- Servlet mapping `/`
- Active Spring profile

### DispatcherServlet

Spring MVC runs through the DispatcherServlet. All web requests are mapped through:

```java
return new String[] { "/" };
```

This means the application uses standard Java web application request handling.

### Servlet API

Configured in `pom.xml`:

```xml
javax.servlet-api
javax.servlet.jsp-api
jstl
```

These dependencies support:

- Servlet request/response handling
- JSP rendering
- JSTL tag usage
- Tomcat deployment

## 9. Where Spring Security Is Used

Spring Security is used for authentication, authorization, CSRF protection, and role-based navigation.

### Security configuration

File:

```text
src/main/java/com/smartclinic/config/SecurityConfig.java
```

This class configures:

- Login page
- Login processing URL
- Logout URL
- Role-based route access
- BCrypt password encoding
- Access denied page
- CSRF protection

### Login flow

Login page:

```text
GET /login
```

Login form submits to:

```text
POST /authenticateTheUser
```

After successful login:

```text
/dashboard
```

Then `AuthController.java` redirects by role:

| Role | Redirect |
| --- | --- |
| ADMIN | `/admin/dashboard` |
| DOCTOR | `/doctor/schedule` |
| RECEPTIONIST | `/appointments/queue` |
| PHARMACIST | `/pharmacy/queue` |

### Password encoding

The project uses:

```java
BCryptPasswordEncoder
```

Passwords in the database are stored as BCrypt hashes.

### Role-based URL access

Examples:

```text
/admin/**       -> ADMIN
/doctor/**      -> DOCTOR
/pharmacy/**    -> PHARMACIST
/appointments/** -> RECEPTIONIST or ADMIN
```

This is important to show because it proves the project has real authentication and authorization, not only static pages.

## 10. Where Docker Is Used

Docker is used to run the complete application stack consistently.

### Dockerfile

File:

```text
Dockerfile
```

The Dockerfile uses a multi-stage build:

1. Maven image builds the WAR
2. Tomcat image runs the WAR

The WAR is deployed as:

```text
/usr/local/tomcat/webapps/ROOT.war
```

That is why the app opens directly at:

```text
http://localhost:8080/login
```

instead of needing:

```text
/smart-clinic
```

### Docker Compose

File:

```text
docker-compose.yml
```

Services:

| Service | Purpose |
| --- | --- |
| `app` | Tomcat running the SmartClinic WAR |
| `db` | MySQL 8 database |

Ports:

| Service | Host port | Container port |
| --- | --- | --- |
| App | `8080` | `8080` |
| MySQL | `3308` | `3306` |

Healthchecks:

- MySQL healthcheck uses `mysqladmin ping`
- App healthcheck calls `http://localhost:8080/login`

This makes the app wait until the database is healthy before starting.

### Smoke test

File:

```text
scripts/docker-smoke.ps1
```

This script:

- Validates Compose config
- Starts the Docker stack
- Waits for `/login`
- Logs in as admin
- Confirms the admin dashboard loads

## 11. Main Functional Modules

### Admin module

Files:

```text
AdminController.java
src/main/webapp/WEB-INF/views/admin
```

Features:

- Admin dashboard metrics
- User management
- Doctor management
- Doctor availability and leave blocking
- Departments
- Settings
- Daily reports
- Revenue analytics
- CSV exports
- Audit logs

Tech stack shown:

- Spring MVC routes
- JSP pages
- Hibernate service/DAO queries
- MySQL persisted data
- Role-based security

### Reception module

Files:

```text
PatientController.java
AppointmentController.java
src/main/webapp/WEB-INF/views/patients
src/main/webapp/WEB-INF/views/appointments
```

Features:

- Patient registration
- Patient search/edit/history
- Appointment booking
- Queue token workflow
- Check-in/no-show
- Reschedule/cancel
- Calendar
- Waitlist
- Mock reminders
- Emergency override

Tech stack shown:

- Form handling in Spring MVC
- JSP forms with CSRF protection
- Hibernate relationships between patients, doctors, appointments, and audit logs
- MySQL demo data

### Doctor module

Files:

```text
DoctorController.java
src/main/webapp/WEB-INF/views/doctor
```

Features:

- Doctor schedule
- Doctor profile
- Consultation workspace
- Vitals
- Diagnosis tags
- Clinical templates
- Lab orders
- Follow-up days
- Risk flags
- Prescription items
- Draft consultation save
- Complete consultation
- Completed consultation history

Tech stack shown:

- Java service logic
- Spring MVC form binding
- Hibernate persistence for prescription and appointment status
- MySQL billing generation after consultation completion

### Pharmacy module

Files:

```text
PharmacyController.java
src/main/webapp/WEB-INF/views/pharmacy
```

Features:

- Prescription dispensing queue
- Medicine inventory
- Batch number
- Expiry date
- Supplier
- Substitution suggestions
- Restocking
- Stock movement history
- Purchase order draft

Tech stack shown:

- Spring MVC controller actions
- Hibernate entity updates
- MySQL inventory persistence
- JSP tables and forms

### Billing module

Files:

```text
BillingController.java
BillingServiceImpl.java
src/main/webapp/WEB-INF/views/billing
```

Features:

- Bill generation
- Payment status
- Payment mode
- Partial payments
- Insurance claim tracking
- Discounts
- Refunds
- Invoice PDF
- Receipt PDF

Tech stack shown:

- Java service business rules
- Hibernate persistence
- MySQL billing table
- iText PDF generation
- JSP invoice view

## 12. Example End-to-End Flow

This is a strong flow to explain in the interview:

### Appointment booking flow

1. Receptionist logs in.
2. Browser opens `/appointments/book`.
3. `AppointmentController` loads doctors and patients.
4. JSP renders the booking form.
5. Receptionist selects patient, doctor, priority, and slot.
6. Form submits to `POST /appointments/book`.
7. Controller receives form data.
8. Controller calls `AppointmentService`.
9. Service validates and prepares appointment data.
10. DAO uses Hibernate `SessionFactory`.
11. Hibernate inserts appointment into MySQL.
12. Audit log is written.
13. User is redirected to the queue page.

This single flow demonstrates:

- JSP form
- Spring MVC controller
- Java service layer
- Hibernate ORM
- MySQL persistence
- Spring Security session
- Audit logging

### Consultation to billing flow

1. Doctor logs in.
2. Doctor opens schedule.
3. Doctor starts consultation.
4. JSP consultation form captures vitals, diagnosis, labs, risk flags, and medicines.
5. Controller posts consultation data to `DoctorController`.
6. `PrescriptionService` saves prescription and items.
7. Appointment status is changed to `COMPLETED`.
8. `BillingService` generates the bill.
9. Billing data is stored in MySQL.
10. Admin can view the invoice and revenue report.

This flow demonstrates business logic across multiple entities.

## 13. Database Design Summary

Important relationships:

```text
users -> doctors
patients -> appointments
doctors -> appointments
appointments -> prescriptions
prescriptions -> prescription_items
appointments -> billing
doctors -> doctor_leaves
patients -> appointment_waitlist
doctors -> appointment_waitlist
users -> audit_log
medicine_inventory -> stock_movements
```

Why this matters:

- It shows normalized relational design.
- It shows foreign key usage.
- It shows realistic hospital workflows.
- It gives enough data for a meaningful internship demo.

## 14. Testing

Tests are under:

```text
src/test/java
```

Current focused tests cover:

- Appointment service behavior
- Slot allocation utility
- Medicine inventory service behavior

Command:

```bash
mvn test
```

Current verification result:

```text
Tests run: 10, Failures: 0, Errors: 0, Skipped: 0
```

## 15. Why This Project Is Internship Worthy

This project is stronger than a basic CRUD application because it demonstrates:

- Real Java web application structure
- MVC architecture
- Role-based authentication
- MySQL relational database design
- Hibernate ORM relationships
- Service and DAO separation
- JSP-based UI
- Business workflows for multiple user roles
- Docker deployment
- Seed data for demo readiness
- Unit tests
- Audit logging
- Reports and exports
- Billing and pharmacy workflows

For an internship interview, it clearly maps to the required skills:

| Required skill | How this project demonstrates it |
| --- | --- |
| Java | Controllers, services, DAOs, entities, utilities, tests |
| MySQL | Schema, seed data, foreign keys, stored procedure, Docker database |
| JSP | Role-based pages under `WEB-INF/views` |
| Spring MVC | Annotated controllers, request mappings, form handling, REST APIs |
| Hibernate ORM | Entity mappings, SessionFactory, DAO layer, transactions |
| J2EE | WAR packaging, Servlet initializer, Tomcat deployment, JSP/Servlet APIs |

## 16. Honest Future Improvements

These are good points to mention if asked what you would improve next:

- Add integration tests using a real MySQL container.
- Introduce DTOs for REST APIs instead of returning entity-shaped JSON.
- Add Flyway or Liquibase for versioned database migrations.
- Add pagination to every large list consistently.
- Add password reset and email/SMS provider integration.
- Add a production deployment pipeline.
- Add more detailed input validation and server-side error messages.

These are future improvements, not blockers for an internship demo.

## 17. Short Interview Explanation

Use this if the interviewer asks: "Explain your project."

"SmartClinic is a Java/J2EE hospital management system built with Spring MVC, JSP, Hibernate ORM, and MySQL. It has role-based login for admin, doctor, receptionist, and pharmacist. The UI is built using JSP pages, requests are handled by Spring MVC controllers, business rules are kept in service classes, and database operations go through Hibernate DAOs. MySQL stores users, patients, doctors, appointments, prescriptions, billing, inventory, and audit logs. The project is packaged as a WAR and deployed to Tomcat through Docker Compose, with MySQL running in a second container. I also added realistic seed data, tests, and a smoke script to verify the Docker setup and default admin login."
