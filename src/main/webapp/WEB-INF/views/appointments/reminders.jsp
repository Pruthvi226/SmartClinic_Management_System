<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="../layout/header.jsp" />

<div class="card">
    <div style="display:flex; justify-content:space-between; align-items:center;">
        <h2 style="margin:0;">Reminder Log</h2>
        <a href="<c:url value='/appointments/queue'/>" class="btn btn-secondary">Queue</a>
    </div>

    <table style="margin-top:1.5rem;">
        <thead><tr><th>When</th><th>Patient</th><th>Channel</th><th>Recipient</th><th>Status</th><th>Message</th></tr></thead>
        <tbody>
            <c:forEach var="r" items="${reminders}">
                <tr>
                    <td><fmt:formatDate value="${r.createdAt}" type="both" pattern="dd MMM yyyy, HH:mm"/></td>
                    <td>${r.appointment.patient.name}</td>
                    <td>${r.channel}</td>
                    <td>${r.recipient}</td>
                    <td><span class="badge badge-normal">${r.status}</span></td>
                    <td>${r.message}</td>
                </tr>
            </c:forEach>
            <c:if test="${empty reminders}">
                <tr><td colspan="6" style="text-align:center; padding:3rem; color:var(--text-muted);">No reminders logged yet.</td></tr>
            </c:if>
        </tbody>
    </table>
</div>

<jsp:include page="../layout/footer.jsp" />
