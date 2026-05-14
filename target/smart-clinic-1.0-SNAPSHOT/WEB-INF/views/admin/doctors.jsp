<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../layout/header.jsp" />

<div class="card">
    <div style="display:flex; justify-content:space-between; align-items:center; margin-bottom: 1.5rem;">
        <h2 style="margin:0;">Manage Doctors</h2>
        <a href="<c:url value='/admin/doctors/add'/>" class="btn btn-primary">+ Add New Doctor</a>
    </div>

    <table>
        <thead>
            <tr>
                <th>ID</th>
                <th>Name</th>
                <th>Email</th>
                <th>Specialization</th>
                <th>Available Days</th>
                <th>Slot Duration</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="doctor" items="${doctors}">
            <tr>
                <td>${doctor.id}</td>
                <td>${doctor.user.name}</td>
                <td>${doctor.user.email}</td>
                <td>${doctor.specialization}</td>
                <td>${doctor.availableDays}</td>
                <td>${doctor.slotDurationMins} mins</td>
            </tr>
            </c:forEach>
            <c:if test="${empty doctors}">
            <tr><td colspan="6">No doctors found in system.</td></tr>
            </c:if>
        </tbody>
    </table>
</div>

<jsp:include page="../layout/footer.jsp" />
