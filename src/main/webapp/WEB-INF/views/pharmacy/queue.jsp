<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="../layout/header.jsp" />

<div class="card">
    <h2>Pharmacy Dispensation Queue</h2>

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
                <td>#PRE-${p.id}</td>
                <td>${p.patient.name}</td>
                <td>Dr. ${p.doctor.user.name}</td>
                <td><fmt:formatDate value="${p.issuedAt}" type="both" pattern="dd MMM yyyy, HH:mm"/></td>
                <td>${p.items.size()} items</td>
                <td>
                    <button class="btn btn-primary" onclick="alert('Dispense action completed.')">View & Dispense</button>
                </td>
            </tr>
            </c:forEach>
            <c:if test="${empty prescriptions}">
            <tr><td colspan="6">No prescriptions in queue.</td></tr>
            </c:if>
        </tbody>
    </table>
</div>

<jsp:include page="../layout/footer.jsp" />
