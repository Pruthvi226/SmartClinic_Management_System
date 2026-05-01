<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="../layout/header.jsp" />

<div class="breadcrumb-container">
    <a href="<c:url value='/dashboard'/>" class="btn btn-secondary" style="padding: 0.4rem 0.8rem; font-size: 0.875rem;">
        <svg class="icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="m15 18-6-6 6-6"/></svg>
        Back to Dashboard
    </a>
</div>

<div class="card">
    <div style="display:flex; justify-content:space-between; align-items:center;">
        <h2 style="margin:0">Pharmacy Dispensation Queue</h2>
        <div style="background:var(--secondary); color:white; padding:0.5rem 1rem; border-radius:12px; font-weight:700; font-size:0.875rem; display:flex; align-items:center; gap:0.5rem;">
            <svg class="icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="m10.5 20.5 10-10a4.95 4.95 0 1 0-7-7l-10 10a4.95 4.95 0 1 0 7 7Z"/><path d="m8.5 8.5 7 7"/></svg>
            LIVE
        </div>
    </div>

    <table>
        <thead>
            <tr>
                <th>Prescription ID</th>
                <th>Patient Name</th>
                <th>Doctor</th>
                <th>Issued At</th>
                <th>Items Count</th>
                <th>Action</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="p" items="${prescriptions}">
            <tr>
                <td><strong style="color:var(--text-muted);">#PRE-${p.id}</strong></td>
                <td><strong>${p.patient.name}</strong></td>
                <td>Dr. ${p.doctor.user.name}</td>
                <td><fmt:formatDate value="${p.issuedAt}" type="both" pattern="dd MMM yyyy, HH:mm"/></td>
                <td><span class="badge" style="background:#E2E8F0; color:var(--text-dark);">${p.items.size()} Items</span></td>
                <td>
                    <button class="btn btn-primary" style="padding:0.4rem 0.8rem; font-size:0.8rem;" onclick="alert('Dispense action completed.')">
                        <svg class="icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="m10.5 20.5 10-10a4.95 4.95 0 1 0-7-7l-10 10a4.95 4.95 0 1 0 7 7Z"/><path d="m8.5 8.5 7 7"/></svg>
                        View & Dispense
                    </button>
                </td>
            </tr>
            </c:forEach>
            <c:if test="${empty prescriptions}">
            <tr><td colspan="6" style="text-align:center; padding:3rem; color:var(--text-muted);">No pending prescriptions in the queue.</td></tr>
            </c:if>
        </tbody>
    </table>
</div>

<jsp:include page="../layout/footer.jsp" />
