<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../layout/header.jsp" />

<div class="card">
    <div style="display:flex; justify-content:space-between; align-items:center; margin-bottom: 1.5rem;">
        <h2 style="margin:0;">Manage Doctors</h2>
        <a href="<c:url value='/admin/doctors/add'/>" class="btn btn-primary">+ Add New Doctor</a>
    </div>

    <c:if test="${param.updated != null}">
        <div style="color:#16A34A; background:#F0FDF4; border:1px solid #DCFCE7; padding:1rem; border-radius:8px; margin-bottom:1rem;">
            Doctor availability updated successfully.
        </div>
    </c:if>

    <table>
        <thead>
            <tr>
                <th>ID</th>
                <th>Name</th>
                <th>Email</th>
                <th>Specialization</th>
                <th>Available Days</th>
                <th>Slot Duration</th>
                <th>Actions</th>
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
                <td>
                    <a href="<c:url value='/admin/doctors/${doctor.id}/edit'/>" class="btn btn-secondary" style="padding:0.35rem 0.7rem; font-size:0.8rem;">Edit</a>
                    <a href="<c:url value='/admin/doctors/${doctor.id}/leaves'/>" class="btn btn-secondary" style="padding:0.35rem 0.7rem; font-size:0.8rem;">Leaves</a>
                </td>
            </tr>
            </c:forEach>
            <c:if test="${empty doctors}">
            <tr><td colspan="7">No doctors found in system.</td></tr>
            </c:if>
        </tbody>
    </table>
</div>

<jsp:include page="../layout/footer.jsp" />
