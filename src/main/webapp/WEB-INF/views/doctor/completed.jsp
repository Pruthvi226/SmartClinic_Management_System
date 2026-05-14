<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="../layout/header.jsp" />

<div class="card">
    <div style="display:flex; justify-content:space-between; align-items:center;">
        <h2 style="margin:0;">Completed Consultations</h2>
        <a href="<c:url value='/doctor/schedule'/>" class="btn btn-secondary">Schedule</a>
    </div>

    <table style="margin-top:1.5rem;">
        <thead>
            <tr><th>Date</th><th>Patient</th><th>Diagnosis Tag</th><th>Vitals</th><th>Follow-up</th><th>PDF</th></tr>
        </thead>
        <tbody>
            <c:forEach var="p" items="${prescriptions}">
                <c:if test="${!p.draft}">
                    <tr>
                        <td><fmt:formatDate value="${p.issuedAt}" type="both" pattern="dd MMM yyyy, HH:mm"/></td>
                        <td>${p.patient.name}</td>
                        <td>${p.diagnosisTag}</td>
                        <td>BP ${p.bloodPressure}, Pulse ${p.pulse}, SpO2 ${p.spo2}</td>
                        <td><c:out value="${p.followUpDays}"/> days</td>
                        <td><a href="<c:url value='/prescriptions/download/${p.id}'/>" class="btn btn-secondary" style="padding:0.35rem 0.7rem;">PDF</a></td>
                    </tr>
                </c:if>
            </c:forEach>
            <c:if test="${empty prescriptions}">
                <tr><td colspan="6" style="text-align:center; padding:3rem; color:var(--text-muted);">No completed consultations yet.</td></tr>
            </c:if>
        </tbody>
    </table>
</div>

<jsp:include page="../layout/footer.jsp" />
