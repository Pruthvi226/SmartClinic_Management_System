<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../layout/header.jsp" />

<div class="card">
    <div style="display:flex;justify-content:space-between;align-items:center;">
        <h2 style="margin:0">Patient Directory</h2>
        <a href="<c:url value='/patients/register'/>" class="btn btn-primary">+ New Patient</a>
    </div>

    <form action="<c:url value='/patients/search'/>" method="GET" style="margin-top: 1.5rem; display:flex; gap:10px;">
        <input type="text" name="keyword" class="form-control" placeholder="Search by name or phone..." value="${param.keyword}" style="max-width: 400px;"/>
        <button type="submit" class="btn btn-primary">Search</button>
        <a href="<c:url value='/patients/search'/>" class="btn" style="background:#e5e7eb;color:black;">Reset</a>
    </form>

    <table>
        <thead>
            <tr>
                <th>ID</th>
                <th>Name</th>
                <th>Phone</th>
                <th>Blood Group</th>
                <th>Actions</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="p" items="${patients}">
            <tr>
                <td>#${p.id}</td>
                <td><strong>${p.name}</strong></td>
                <td>${p.phone}</td>
                <td>${p.bloodGroup}</td>
                <td>
                    <a href="<c:url value='/patients/${p.id}/history'/>" class="btn" style="background:var(--secondary);color:white;padding:0.3rem 0.6rem;font-size:0.875rem;">History</a>
                    <a href="<c:url value='/appointments/book?patientId=${p.id}'/>" class="btn" style="background:var(--primary);color:white;padding:0.3rem 0.6rem;font-size:0.875rem;">Book Apt</a>
                </td>
            </tr>
            </c:forEach>
            <c:if test="${empty patients}">
            <tr><td colspan="5">No patients found.</td></tr>
            </c:if>
        </tbody>
    </table>
</div>

<jsp:include page="../layout/footer.jsp" />
