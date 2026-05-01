<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="../layout/header.jsp" />

<div class="breadcrumb-container">
    <a href="<c:url value='/patients/search'/>" class="btn btn-secondary" style="padding: 0.4rem 0.8rem; font-size: 0.875rem;">
        <svg class="icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="m15 18-6-6 6-6"/></svg>
        Back to Directory
    </a>
</div>

<div class="card">
    <div style="display:flex;justify-content:space-between;align-items:center;">
        <div style="display:flex; align-items:center; gap:1rem;">
            <div style="background:var(--primary); width:48px; height:48px; border-radius:12px; display:flex; align-items:center; justify-content:center; color:white;">
                <svg class="icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M19 21v-2a4 4 0 0 0-4-4H9a4 4 0 0 0-4 4v2"/><circle cx="12" cy="7" r="4"/></svg>
            </div>
            <div>
                <h2 style="margin:0">${patient.name}</h2>
                <span style="color:var(--text-muted); font-size:0.875rem;">Medical Record #PAT-${patient.id} | ${patient.gender}, ${patient.age} years | Blood: ${patient.bloodGroup}</span>
            </div>
        </div>
        <a href="<c:url value='/appointments/book?patientId=${patient.id}'/>" class="btn btn-primary">
            <svg class="icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><rect x="3" y="4" width="18" height="18" rx="2" ry="2"/><line x1="16" y1="2" x2="16" y2="6"/><line x1="8" y1="2" x2="8" y2="6"/><line x1="3" y1="10" x2="21" y2="10"/></svg>
            Book Appointment
        </a>
    </div>

    <div style="display:grid; grid-template-columns: 1fr 1fr; gap:2rem; margin-top:2.5rem;">
        <div>
            <h3 style="display:flex; align-items:center; gap:0.5rem; font-size:1.1rem; margin-bottom:1rem;">
                <svg class="icon" style="color:var(--primary);" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><rect x="3" y="4" width="18" height="18" rx="2" ry="2"/><line x1="16" y1="2" x2="16" y2="6"/><line x1="8" y1="2" x2="8" y2="6"/><line x1="3" y1="10" x2="21" y2="10"/></svg>
                Appointment History
            </h3>
            <table>
                <thead>
                    <tr><th>Date & Time</th><th>Doctor</th><th>Status</th></tr>
                </thead>
                <tbody>
                    <c:forEach var="a" items="${appointments}">
                    <tr>
                        <td><strong><fmt:formatDate value="${a.slotDatetime}" type="both" pattern="dd MMM yyyy, HH:mm"/></strong></td>
                        <td>Dr. ${a.doctor.user.name}</td>
                        <td><span class="badge badge-normal">${a.status}</span></td>
                    </tr>
                    </c:forEach>
                    <c:if test="${empty appointments}">
                        <tr><td colspan="3" style="text-align:center; padding:2rem; color:var(--text-muted);">No past appointments.</td></tr>
                    </c:if>
                </tbody>
            </table>
        </div>
        <div>
            <h3 style="display:flex; align-items:center; gap:0.5rem; font-size:1.1rem; margin-bottom:1rem;">
                <svg class="icon" style="color:var(--secondary);" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M14.5 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V7.5L14.5 2z"/><polyline points="14 2 14 8 20 8"/></svg>
                Prescription History
            </h3>
            <table>
                <thead>
                    <tr><th>Date</th><th>Doctor</th><th>Diagnosis</th></tr>
                </thead>
                <tbody>
                    <c:forEach var="p" items="${prescriptions}">
                    <tr>
                        <td><strong><fmt:formatDate value="${p.issuedAt}" type="both" pattern="dd MMM yyyy"/></strong></td>
                        <td>Dr. ${p.doctor.user.name}</td>
                        <td style="font-style: italic;">${p.diagnosis}</td>
                    </tr>
                    </c:forEach>
                    <c:if test="${empty prescriptions}">
                        <tr><td colspan="3" style="text-align:center; padding:2rem; color:var(--text-muted);">No prescriptions found.</td></tr>
                    </c:if>
                </tbody>
            </table>
        </div>
    </div>
</div>

<jsp:include page="../layout/footer.jsp" />
