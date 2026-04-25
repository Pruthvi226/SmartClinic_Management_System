<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="../layout/header.jsp" />

<div class="card">
    <div style="display:flex;justify-content:space-between;align-items:center;">
        <h2 style="margin:0">My Schedule - Dr. ${doctor.user.name}</h2>
    </div>

    <c:if test="${param.completed != null}">
        <div style="color: var(--normal); background: #D1FAE5; padding: 1rem; border-radius: 8px; margin-top: 1rem;">
            Consultation completed and prescription saved successfully.
        </div>
    </c:if>

    <table>
        <thead>
            <tr>
                <th>Time</th>
                <th>Patient</th>
                <th>Priority</th>
                <th>Notes</th>
                <th>Status</th>
                <th>Action</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="a" items="${appointments}">
            <tr>
                <td><fmt:formatDate value="${a.slotDatetime}" type="time" pattern="HH:mm"/></td>
                <td>${a.patient.name}</td>
                <td>
                    <span class="badge badge-${a.priority.toString().toLowerCase()}">${a.priority}</span>
                </td>
                <td>${a.notes}</td>
                <td>${a.status}</td>
                <td>
                    <c:if test="${a.status == 'SCHEDULED'}">
                        <a href="<c:url value='/doctor/consult/${a.id}'/>" class="btn btn-primary">Start Consult</a>
                    </c:if>
                </td>
            </tr>
            </c:forEach>
            <c:if test="${empty appointments}">
            <tr><td colspan="6">No scheduled appointments for today. Great job!</td></tr>
            </c:if>
        </tbody>
    </table>
</div>

<jsp:include page="../layout/footer.jsp" />
