<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="../layout/header.jsp" />

<div class="breadcrumb-container">
    <a href="<c:url value='/appointments/queue'/>" class="btn btn-secondary" style="padding: 0.4rem 0.8rem; font-size: 0.875rem;">
        <svg class="icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="m15 18-6-6 6-6"/></svg>
        Back to Queue
    </a>
</div>

<div class="card">
    <div style="display:flex; justify-content:space-between; align-items:flex-start; gap:1.5rem;">
        <div>
            <h2 style="margin:0;">Appointment Timeline</h2>
            <p style="color:var(--text-muted); margin:0.5rem 0 0;">
                #APT-${appointment.id} | ${appointment.patient.name} with Dr. ${appointment.doctor.user.name}
            </p>
        </div>
        <div style="text-align:right;">
            <div style="font-weight:700; color:var(--primary);"><fmt:formatDate value="${appointment.slotDatetimeAsDate}" type="both" pattern="dd MMM yyyy, HH:mm"/></div>
            <div style="color:var(--text-muted); font-size:0.875rem;">${appointment.status} | ${appointment.priority}</div>
        </div>
    </div>

    <table style="margin-top:2rem;">
        <thead>
            <tr>
                <th>Time</th>
                <th>User</th>
                <th>Event</th>
                <th>IP Address</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="log" items="${logs}">
                <tr>
                    <td><fmt:formatDate value="${log.timestamp}" type="both" pattern="dd MMM yyyy, HH:mm:ss"/></td>
                    <td>${log.user != null ? log.user.email : 'System'}</td>
                    <td><strong>${log.action}</strong></td>
                    <td>${log.ipAddress}</td>
                </tr>
            </c:forEach>
            <c:if test="${empty logs}">
                <tr>
                    <td colspan="4" style="text-align:center; padding:3rem; color:var(--text-muted);">
                        No business events logged for this appointment yet.
                    </td>
                </tr>
            </c:if>
        </tbody>
    </table>
</div>

<jsp:include page="../layout/footer.jsp" />
