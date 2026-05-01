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
    <div style="display:flex;justify-content:space-between;align-items:center;">
        <h2 style="margin:0">My Daily Schedule</h2>
        <div style="text-align:right;">
            <div style="font-weight:600; color:var(--primary);">Dr. ${doctor.user.name}</div>
            <div style="font-size:0.875rem; color:var(--text-muted);">${doctor.specialization}</div>
        </div>
    </div>

    <c:if test="${param.completed != null}">
        <div style="color: #16A34A; background: #F0FDF4; border: 1px solid #DCFCE7; padding: 1rem; border-radius: 12px; margin-top: 1.5rem; display: flex; align-items: center; gap: 0.5rem;">
            <svg class="icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><polyline points="20 6 9 17 4 12"/></svg>
            Consultation completed and prescription saved successfully.
        </div>
    </c:if>

    <table>
        <thead>
            <tr>
                <th>Time</th>
                <th>Patient Name</th>
                <th>Priority</th>
                <th>Status</th>
                <th>Actions</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="a" items="${appointments}">
            <tr>
                <td><strong style="color:var(--primary);"><fmt:formatDate value="${a.slotDatetime}" type="time" pattern="HH:mm"/></strong></td>
                <td>
                    <div style="font-weight:600;">${a.patient.name}</div>
                    <div style="font-size:0.75rem; color:var(--text-muted);">#PAT-${a.patient.id}</div>
                </td>
                <td>
                    <c:set var="pClass" value="badge-normal" />
                    <c:if test="${a.priority == 'EMERGENCY'}"><c:set var="pClass" value="badge-emergency" /></c:if>
                    <c:if test="${a.priority == 'SENIOR'}"><c:set var="pClass" value="badge-senior" /></c:set>
                    <span class="badge ${pClass}">${a.priority}</span>
                </td>
                <td><span class="badge" style="background:#F1F5F9; color:var(--text-dark);">${a.status}</span></td>
                <td>
                    <c:if test="${a.status == 'SCHEDULED'}">
                        <a href="<c:url value='/doctor/consult/${a.id}'/>" class="btn btn-primary" style="padding:0.4rem 0.8rem; font-size:0.8rem;">
                            <svg class="icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M2 3h6a4 4 0 0 1 4 4v14a3 3 0 0 0-3-3H2z"/><path d="M22 3h-6a4 4 0 0 0-4 4v14a3 3 0 0 1 3-3h7z"/></svg>
                            Start Consult
                        </a>
                    </c:if>
                    <c:if test="${a.status == 'COMPLETED'}">
                        <span style="color:var(--secondary); font-size:0.875rem; font-weight:600; display:flex; align-items:center; gap:0.25rem;">
                            <svg class="icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><polyline points="20 6 9 17 4 12"/></svg>
                            Done
                        </span>
                    </c:if>
                </td>
            </tr>
            </c:forEach>
            <c:if test="${empty appointments}">
            <tr><td colspan="5" style="text-align:center; padding:3rem; color:var(--text-muted);">No scheduled appointments for today. Take a break!</td></tr>
            </c:if>
        </tbody>
    </table>
</div>

<jsp:include page="../layout/footer.jsp" />
