<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="../layout/header.jsp" />

<div class="card">
    <div style="display:flex; justify-content:space-between; align-items:center;">
        <h2 style="margin:0;">Appointment Calendar</h2>
        <form action="<c:url value='/appointments/calendar'/>" method="get" style="display:flex; gap:0.5rem; margin:0;">
            <input type="date" name="date" class="form-control" value="${selectedDate}" />
            <button class="btn btn-secondary">View</button>
        </form>
    </div>

    <table style="margin-top:1.5rem;">
        <thead><tr><th>Time</th><th>Token</th><th>Patient</th><th>Doctor</th><th>Priority</th><th>Queue</th></tr></thead>
        <tbody>
            <c:forEach var="a" items="${appointments}">
                <tr>
                    <td><fmt:formatDate value="${a.slotDatetimeAsDate}" type="time" pattern="HH:mm"/></td>
                    <td><span class="badge badge-normal">${a.tokenNumber}</span></td>
                    <td>${a.patient.name}</td>
                    <td>Dr. ${a.doctor.user.name}</td>
                    <td>${a.priority}</td>
                    <td>${a.queueStateLabel}</td>
                </tr>
            </c:forEach>
            <c:if test="${empty appointments}">
                <tr><td colspan="6" style="text-align:center; padding:3rem; color:var(--text-muted);">No appointments for this date.</td></tr>
            </c:if>
        </tbody>
    </table>
</div>

<jsp:include page="../layout/footer.jsp" />
