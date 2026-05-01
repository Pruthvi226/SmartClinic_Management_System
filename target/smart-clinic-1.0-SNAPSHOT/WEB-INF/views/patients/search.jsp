<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../layout/header.jsp" />

<div class="breadcrumb-container">
    <a href="<c:url value='/dashboard'/>" class="btn btn-secondary" style="padding: 0.4rem 0.8rem; font-size: 0.875rem;">
        <svg class="icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="m15 18-6-6 6-6"/></svg>
        Back to Dashboard
    </a>
</div>

<div class="card">
    <div style="display:flex;justify-content:space-between;align-items:center;">
        <h2 style="margin:0">Patient Directory</h2>
        <a href="<c:url value='/patients/register'/>" class="btn btn-primary">
            <svg class="icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M12 5v14M5 12h14"/></svg>
            New Patient
        </a>
    </div>

    <form action="<c:url value='/patients/search'/>" method="GET" style="margin-top: 1.5rem; display:flex; gap:10px;">
        <div style="position:relative; flex:1; max-width:400px;">
            <svg class="icon" style="position:absolute; left:12px; top:50%; transform:translateY(-50%); color:var(--text-muted);" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><circle cx="11" cy="11" r="8"/><path d="m21 21-4.3-4.3"/></svg>
            <input type="text" name="keyword" class="form-control" placeholder="Search by name or phone..." value="${param.keyword}" style="padding-left:40px;"/>
        </div>
        <button type="submit" class="btn btn-primary">Search</button>
        <c:if test="${not empty param.keyword}">
            <a href="<c:url value='/patients/search'/>" class="btn btn-secondary">Reset</a>
        </c:if>
    </form>

    <table>
        <thead>
            <tr>
                <th>Patient ID</th>
                <th>Full Name</th>
                <th>Phone</th>
                <th>Blood Group</th>
                <th>Actions</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="p" items="${patients}">
            <tr>
                <td><span style="color:var(--text-muted); font-weight:600;">#PAT-${p.id}</span></td>
                <td><strong>${p.name}</strong></td>
                <td>${p.phone}</td>
                <td><span class="badge badge-normal">${p.bloodGroup}</span></td>
                <td style="display:flex; gap:8px;">
                    <a href="<c:url value='/patients/${p.id}/history'/>" class="btn btn-secondary" style="padding:0.4rem 0.8rem; font-size:0.8rem;">
                        <svg class="icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"/><polyline points="14 2 14 8 20 8"/><line x1="16" y1="13" x2="8" y2="13"/><line x1="16" y1="17" x2="8" y2="17"/><polyline points="10 9 9 9 8 9"/></svg>
                        History
                    </a>
                    <a href="<c:url value='/appointments/book?patientId=${p.id}'/>" class="btn btn-primary" style="padding:0.4rem 0.8rem; font-size:0.8rem;">
                        <svg class="icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><rect x="3" y="4" width="18" height="18" rx="2" ry="2"/><line x1="16" y1="2" x2="16" y2="6"/><line x1="8" y1="2" x2="8" y2="6"/><line x1="3" y1="10" x2="21" y2="10"/></svg>
                        Book Appointment
                    </a>
                </td>
            </tr>
            </c:forEach>
            <c:if test="${empty patients}">
            <tr><td colspan="5" style="text-align:center; padding:3rem; color:var(--text-muted);">No patients found matching your search.</td></tr>
            </c:if>
        </tbody>
    </table>
</div>

<jsp:include page="../layout/footer.jsp" />
