<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="../layout/header.jsp" />

<div class="card">
    <div style="display:flex;justify-content:space-between;align-items:center;">
        <h2>Patient History: ${patient.name}</h2>
        <a href="<c:url value='/appointments/book?patientId=${patient.id}'/>" class="btn btn-primary">Book New Appointment</a>
    </div>
    <div style="display:grid; grid-template-columns: 1fr 1fr; gap:20px; margin-top:20px;">
        <div>
            <h3>Appointments</h3>
            <table>
                <tr><th>Date</th><th>Doctor</th><th>Status</th></tr>
                <c:forEach var="a" items="${appointments}">
                <tr>
                    <td><fmt:formatDate value="${a.slotDatetime}" type="both" pattern="dd MMM yyyy, HH:mm"/></td>
                    <td>Dr. ${a.doctor.user.name}</td>
                    <td>${a.status}</td>
                </tr>
                </c:forEach>
            </table>
        </div>
        <div>
            <h3>Prescriptions</h3>
            <table>
                <tr><th>Date</th><th>Doctor</th><th>Diagnosis</th></tr>
                <c:forEach var="p" items="${prescriptions}">
                <tr>
                    <td><fmt:formatDate value="${p.issuedAt}" type="both" pattern="dd MMM yyyy"/></td>
                    <td>Dr. ${p.doctor.user.name}</td>
                    <td>${p.diagnosis}</td>
                </tr>
                </c:forEach>
            </table>
        </div>
    </div>
</div>

<jsp:include page="../layout/footer.jsp" />
