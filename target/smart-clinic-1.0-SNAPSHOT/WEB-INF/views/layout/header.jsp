<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>SmartClinic | Hospital Management</title>
    <link rel="stylesheet" href="<c:url value='/resources/css/style.css'/>">
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="<c:url value='/resources/js/app.js'/>"></script>
</head>
<body>
    <nav class="navbar">
        <a href="<c:url value='/dashboard'/>" class="navbar-brand">
            <div style="background: var(--primary); color: white; padding: 6px; border-radius: 10px; display: flex;">
                <svg class="icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round"><path d="M22 12h-4l-3 9L9 3l-3 9H2"/></svg>
            </div>
            <span>SmartClinic</span>
        </a>
        <div class="nav-links">
            <sec:authorize access="isAuthenticated()">
                <sec:authorize access="hasAuthority('ADMIN')">
                    <a href="<c:url value='/admin/dashboard'/>">
                        <svg class="icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><rect x="3" y="3" width="18" height="18" rx="2" ry="2"/><line x1="3" y1="9" x2="21" y2="9"/><line x1="9" y1="21" x2="9" y2="9"/></svg>
                        Admin
                    </a>
                </sec:authorize>
                <sec:authorize access="hasAuthority('RECEPTIONIST') or hasAuthority('ADMIN')">
                    <a href="<c:url value='/patients/search'/>">
                        <svg class="icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2"/><circle cx="9" cy="7" r="4"/><path d="M23 21v-2a4 4 0 0 0-3-3.87"/><path d="M16 3.13a4 4 0 0 1 0 7.75"/></svg>
                        Patients
                    </a>
                    <a href="<c:url value='/appointments/queue'/>">
                        <svg class="icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"/><polyline points="14 2 14 8 20 8"/><line x1="16" y1="13" x2="8" y2="13"/><line x1="16" y1="17" x2="8" y2="17"/><polyline points="10 9 9 9 8 9"/></svg>
                        Queue
                    </a>
                </sec:authorize>
                <sec:authorize access="hasAuthority('DOCTOR')">
                    <a href="<c:url value='/doctor/schedule'/>">
                        <svg class="icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><rect x="3" y="4" width="18" height="18" rx="2" ry="2"/><line x1="16" y1="2" x2="16" y2="6"/><line x1="8" y1="2" x2="8" y2="6"/><line x1="3" y1="10" x2="21" y2="10"/></svg>
                        My Schedule
                    </a>
                </sec:authorize>
                <sec:authorize access="hasAuthority('PHARMACIST')">
                    <a href="<c:url value='/pharmacy/queue'/>">
                        <svg class="icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="m10.5 20.5 10-10a4.95 4.95 0 1 0-7-7l-10 10a4.95 4.95 0 1 0 7 7Z"/><path d="m8.5 8.5 7 7"/></svg>
                        Pharmacy
                    </a>
                </sec:authorize>

                <form action="<c:url value='/logout'/>" method="post" style="display:inline; margin-left: 0.5rem;">
                    <button type="submit" class="btn btn-secondary" style="padding: 0.4rem 0.8rem; font-size: 0.875rem;">
                        <svg class="icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M9 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h4"/><polyline points="16 17 21 12 16 7"/><line x1="21" y1="12" x2="9" y2="12"/></svg>
                        Logout
                    </button>
                </form>
            </sec:authorize>
        </div>
    </nav>
    <div class="container">
        <div class="breadcrumb-container" id="breadcrumb">
            <!-- Dynamic breadcrumbs or back button will be injected here or added statically in pages -->
        </div>
