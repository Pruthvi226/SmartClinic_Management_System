# SmartClinic Internship Demo Guide

## Project Snapshot

SmartClinic Management System is a Java/J2EE hospital management system built as a Maven WAR application. It uses Spring MVC controllers, JSP/JSTL views, Spring Security role-based access, Hibernate ORM entities/DAOs/services, and MySQL persistence.

For a detailed architecture and tech stack explanation, see `PROJECT_EXPLANATION.md`.

## Verified Docker Setup

- App URL: `http://localhost:8080/login`
- App container port: `8080`
- MySQL host port: `3308`
- MySQL container port: `3306`
- Compose file: no deprecated `version` attribute
- Healthchecks: MySQL uses `mysqladmin ping`; app checks `http://localhost:8080/login`

If you see the raw Tomcat 404 page on `localhost:8084`, that is the old/wrong port. Use `http://localhost:8080/login` for this demo.

## Tech Stack Match

| Requirement | In project |
| --- | --- |
| Java | Java 17 source/target in `pom.xml` |
| MySQL | MySQL 8 schema, Docker service, Connector/J |
| JSP | JSP views under `src/main/webapp/WEB-INF/views` |
| Spring MVC | `WebConfig`, annotated controllers, REST controllers |
| Hibernate ORM | JPA entities plus Hibernate SessionFactory/DAOs |
| J2EE | Servlet initializer, filters, WAR packaging, JSP/JSTL |

## Demo Login Accounts

| Role | Email | Password |
| --- | --- | --- |
| Admin | `admin@smartclinic.com` | `admin123` |
| Doctor | `doctor@smartclinic.com` | `doctor123` |
| Receptionist | `reception@smartclinic.com` | `reception123` |
| Pharmacist | `pharmacy@smartclinic.com` | `pharmacy123` |

## Two-Minute Presentation Script

Start with: "This is SmartClinic Management System, a role-based Java/J2EE hospital management application running on Tomcat through Docker. I will show how the same MySQL data flows through Spring MVC controllers, Hibernate ORM services and DAOs, and JSP views."

0:00-0:25: Login as admin. Open the admin dashboard and explain that Spring Security authenticates the user, routes by role, and shows live dashboard counts from MySQL through Hibernate.

0:25-0:50: Open Doctors, Departments, Settings, Reports, and Revenue Analytics. Point out CRUD screens, CSV exports, doctor availability, leave blocking, billing metrics, and audit logs as Spring MVC controller workflows rendered by JSP.

0:50-1:15: Login as reception. Show patient search/register, appointment booking, queue, calendar, waitlist, reminders, reschedule/cancel, and emergency override. Explain that appointment slots are calculated in Java and persisted in MySQL.

1:15-1:40: Login as doctor. Show schedule, profile, consultation form, vitals, diagnosis tags, lab orders, prescription medicines, draft save, completion, and completed consultations. Mention Hibernate relationships between doctor, appointment, patient, prescription, and billing.

1:40-2:00: Login as pharmacy or return to admin billing. Show prescription queue, inventory, stock movement, purchase-order draft, invoice/receipt PDFs, insurance, discounts, and refunds. Close by saying Docker Compose starts Tomcat plus MySQL with seeded demo data on ports 8080 and 3308.

## Working Demo Flow

1. Login as `admin@smartclinic.com / admin123`.
2. Show admin dashboard counts, doctor management, user management, departments, settings, and audit logs.
3. Edit a doctor from the Manage Doctors page and show configurable available days/slot duration.
4. Open a doctor's Leaves page, block a leave date, and explain that smart slots skip blocked dates.
5. Open Daily Reports and Revenue Analytics to show appointments, payments, discounts, refunds, insurance claims, and CSV exports.
6. Login as `reception@smartclinic.com / reception123`.
7. Register/search/edit a patient, including allergies, then book an appointment using smart recommended slots.
8. When slots are unavailable, open the waitlist flow and add a patient to the waitlist.
9. Use emergency override booking for an urgent case.
10. Use the live queue to check in, mark no-show, send a mock SMS reminder, reschedule, or cancel an appointment.
11. Open Calendar, Reminder Log, and an appointment timeline from the queue.
12. Login as `doctor@smartclinic.com / doctor123`.
13. Open Profile, Schedule, and a consultation. Capture vitals, diagnosis tag, labs, follow-up, risk flags, medicines, and save a draft.
14. Reopen the consultation, complete it, show allergy warning behavior, and then open Completed Consultations.
15. Login as `pharmacy@smartclinic.com / pharmacy123`.
16. Show inventory batch/expiry/supplier/substitution fields, low-stock alerts, expiry watch, movements, and purchase-order draft.
17. Dispense a prescription from the pharmacy queue, show stock reduction, and download the prescription PDF.
18. Return as admin and show billing list filters, insurance claim tracking, discount authorization, refund workflow, invoice PDF, receipt PDF, audit filtering, and pagination.

## What Was Hardened For Demo

- Removed a committed local database password and switched to environment-driven defaults.
- Re-enabled Spring Security CSRF protection for JSP POST forms.
- Made smart slot allocation respect each doctor's configured availability days.
- Fixed a null handling bug in doctor API updates.
- Standardized billing payment status around `PENDING` and `PAID`.
- Replaced console prints and stack traces with SLF4J logging.
- Added a generic error JSP so unexpected web errors show a polished page.
- Added focused JUnit tests for the appointment slot allocator.
- Added admin doctor availability editing without changing the Spring MVC/JSP stack.
- Added receptionist appointment reschedule/cancel workflows from the live queue.
- Added pharmacist medicine inventory, low-stock alerts, restocking, and dispense-based stock reduction.
- Added appointment-specific audit timelines for booking, rescheduling, cancellation, consultation, and dispensing.
- Replaced placeholder admin dashboard tiles with real patient, doctor, low-stock, and audit counts.
- Added doctor leave blocking so unavailable dates return no appointment slots.
- Added appointment waitlist workflow and automatic notification marking after cancellation.
- Added patient allergy capture and medicine-entry warning during consultation.
- Added billing payment modes, partial payment support, references, balances, and invoice updates.
- Added daily operational reports, prescription PDF downloads, audit filters, and pagination on major lists.
- Added doctor clinical workflow: vitals, templates, diagnosis tags, lab orders, follow-up days, risk flags, prescription favorites, draft save, profile, and completed consultation history.
- Added receptionist queue workflow: token numbers, check-in, no-show, emergency override, calendar view, patient edit, and mock reminder logs.
- Added admin operations: user management, departments, settings, revenue analytics, and CSV exports for patients, billing, and audit logs.
- Added pharmacy operations: batch/expiry/supplier/substitution tracking, stock movement history, and purchase-order draft.
- Added billing operations: insurance claim tracking, authorized discounts, refunds, and payment receipt PDFs.

## Current Limitations To Mention Honestly

- The project has focused unit tests that currently pass, but no integration tests against a live MySQL container yet.
- API responses still expose entity-shaped JSON instead of dedicated DTOs.
- Production profile files exist, but deployment should be verified with real managed MySQL credentials.
- The `target/` build output is currently present in the repository history/workspace; future cleanup should stop tracking generated files.

## Commands

```bash
mvn test
docker compose up --build -d
docker compose ps
docker compose logs -f app
```

Open the app:

```text
http://localhost:8080/login
```

Windows smoke test:

```powershell
.\scripts\docker-smoke.ps1 -AppPort 8080 -DbHostPort 3308
```

Reset to a fresh seeded Docker database:

```bash
docker compose down -v
docker compose up --build -d
```

If Docker reports `dockerDesktopLinuxEngine` or cannot connect to the Docker API, start Docker Desktop first and wait for the engine to become ready.
