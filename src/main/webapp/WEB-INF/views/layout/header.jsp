<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>SmartClinic Hospital Management</title>
    <link rel="stylesheet" href="<c:url value='/resources/css/style.css'/>">
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="<c:url value='/resources/js/app.js'/>"></script>
</head>
<body>
    <nav class="navbar">
        <a href="<c:url value='/dashboard'/>" class="navbar-brand">
            <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M22 12h-4l-3 9L9 3l-3 9H2"/></svg>
            SmartClinic
        </a>
        <div class="nav-links">
            <sec:authorize access="isAuthenticated()">
                <sec:authorize access="hasAuthority('ADMIN')">
                    <a href="<c:url value='/admin/dashboard'/>">Admin Dashboard</a>
                    <a href="<c:url value='/admin/audit-log'/>">Audit Logs</a>
                </sec:authorize>
                <sec:authorize access="hasAuthority('RECEPTIONIST') or hasAuthority('ADMIN')">
                    <a href="<c:url value='/patients/search'/>">Patients</a>
                    <a href="<c:url value='/appointments/queue'/>">Queue</a>
                </sec:authorize>
                <sec:authorize access="hasAuthority('DOCTOR')">
                    <a href="<c:url value='/doctor/schedule'/>">My Schedule</a>
                </sec:authorize>
                <sec:authorize access="hasAuthority('PHARMACIST')">
                    <a href="<c:url value='/pharmacy/queue'/>">Pharmacy</a>
                </sec:authorize>

                <form action="<c:url value='/logout'/>" method="post" style="display:inline; margin-left: 1.5rem;">
                    <button type="submit" class="btn btn-primary" style="padding: 0.4rem 1rem;">Logout</button>
                </form>
            </sec:authorize>
        </div>
    </nav>
    <div class="container animate-enter">
