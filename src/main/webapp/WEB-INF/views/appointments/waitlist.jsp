<%@ page contentType="text/html;charset=UTF-8" language="java" import="java.time.LocalDate" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="../layout/header.jsp" />

<div class="breadcrumb-container">
    <a href="<c:url value='/appointments/queue'/>" class="btn btn-secondary" style="padding: 0.4rem 0.8rem; font-size: 0.875rem;">
        <svg class="icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="m15 18-6-6 6-6"/></svg>
        Back to Queue
    </a>
</div>

<div style="display:grid; grid-template-columns: 380px 1fr; gap:1.5rem; align-items:start;">
    <div class="card" style="margin-bottom:0;">
        <h2 style="margin-top:0;">Add Waitlist Entry</h2>
        <p style="color:var(--text-muted);">Use this when preferred doctor slots are full or blocked by leave.</p>

        <c:if test="${param.added != null}">
            <div style="color:#16A34A; background:#F0FDF4; border:1px solid #DCFCE7; padding:0.8rem; border-radius:8px; margin-bottom:1rem;">Patient added to waitlist.</div>
        </c:if>
        <c:if test="${param.updated != null}">
            <div style="color:#16A34A; background:#F0FDF4; border:1px solid #DCFCE7; padding:0.8rem; border-radius:8px; margin-bottom:1rem;">Waitlist status updated.</div>
        </c:if>

        <form action="<c:url value='/appointments/waitlist'/>" method="post">
            <div class="form-group">
                <label>Patient</label>
                <select name="patientId" class="form-control" required>
                    <option value="">-- Select Patient --</option>
                    <c:forEach var="p" items="${patients}">
                        <option value="${p.id}" <c:if test="${selectedPatientId == p.id}">selected</c:if>>${p.name} (${p.phone})</option>
                    </c:forEach>
                </select>
            </div>
            <div class="form-group">
                <label>Doctor</label>
                <select name="doctorId" class="form-control" required>
                    <option value="">-- Select Doctor --</option>
                    <c:forEach var="doc" items="${doctors}">
                        <option value="${doc.id}" <c:if test="${selectedDoctorId == doc.id}">selected</c:if>>Dr. ${doc.user.name} - ${doc.specialization}</option>
                    </c:forEach>
                </select>
            </div>
            <div class="form-group">
                <label>Requested Date</label>
                <input type="date" name="requestedDate" class="form-control" required min="<%= LocalDate.now() %>" value="${selectedDate}" />
            </div>
            <div class="form-group">
                <label>Priority</label>
                <select name="priority" class="form-control" required>
                    <option value="NORMAL" <c:if test="${selectedPriority == 'NORMAL'}">selected</c:if>>Normal</option>
                    <option value="SENIOR" <c:if test="${selectedPriority == 'SENIOR'}">selected</c:if>>Senior Citizen</option>
                    <option value="EMERGENCY" <c:if test="${selectedPriority == 'EMERGENCY'}">selected</c:if>>Emergency</option>
                </select>
            </div>
            <div class="form-group">
                <label>Notes</label>
                <textarea name="notes" class="form-control" rows="3" placeholder="Preferred time, patient constraints"></textarea>
            </div>
            <button type="submit" class="btn btn-primary" style="width:100%;">Add to Waitlist</button>
        </form>
    </div>

    <div class="card" style="margin-bottom:0;">
        <div style="display:flex; justify-content:space-between; align-items:center; gap:1rem;">
            <h2 style="margin:0;">Waitlist</h2>
            <form action="<c:url value='/appointments/waitlist'/>" method="get" style="display:flex; gap:0.5rem; margin:0;">
                <select name="status" class="form-control">
                    <option value="">All Statuses</option>
                    <option value="WAITING" <c:if test="${selectedStatus == 'WAITING'}">selected</c:if>>Waiting</option>
                    <option value="NOTIFIED" <c:if test="${selectedStatus == 'NOTIFIED'}">selected</c:if>>Notified</option>
                    <option value="BOOKED" <c:if test="${selectedStatus == 'BOOKED'}">selected</c:if>>Booked</option>
                    <option value="CANCELLED" <c:if test="${selectedStatus == 'CANCELLED'}">selected</c:if>>Cancelled</option>
                </select>
                <button type="submit" class="btn btn-secondary">Filter</button>
            </form>
        </div>

        <table style="margin-top:1.5rem;">
            <thead>
                <tr>
                    <th>Patient</th>
                    <th>Doctor</th>
                    <th>Date</th>
                    <th>Priority</th>
                    <th>Status</th>
                    <th>Update</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="entry" items="${entries}">
                    <tr>
                        <td><strong>${entry.patient.name}</strong><br><span style="color:var(--text-muted); font-size:0.75rem;">#PAT-${entry.patient.id}</span></td>
                        <td>Dr. ${entry.doctor.user.name}</td>
                        <td>${entry.requestedDate}</td>
                        <td><span class="badge badge-normal">${entry.priority}</span></td>
                        <td><span class="badge" style="background:#E2E8F0; color:var(--text-dark);">${entry.status}</span></td>
                        <td>
                            <form action="<c:url value='/appointments/waitlist/${entry.id}/status'/>" method="post" style="display:flex; gap:0.4rem; margin:0;">
                                <select name="status" class="form-control" style="width:120px;">
                                    <option value="WAITING" <c:if test="${entry.status == 'WAITING'}">selected</c:if>>Waiting</option>
                                    <option value="NOTIFIED" <c:if test="${entry.status == 'NOTIFIED'}">selected</c:if>>Notified</option>
                                    <option value="BOOKED" <c:if test="${entry.status == 'BOOKED'}">selected</c:if>>Booked</option>
                                    <option value="CANCELLED" <c:if test="${entry.status == 'CANCELLED'}">selected</c:if>>Cancel</option>
                                </select>
                                <button type="submit" class="btn btn-secondary" style="padding:0.35rem 0.7rem;">Save</button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
                <c:if test="${empty entries}">
                    <tr><td colspan="6" style="text-align:center; padding:3rem; color:var(--text-muted);">No waitlist entries found.</td></tr>
                </c:if>
            </tbody>
        </table>

        <c:if test="${pageSlice.totalPages > 1}">
            <div style="display:flex; justify-content:flex-end; gap:0.5rem; margin-top:1rem;">
                <c:if test="${pageSlice.hasPrevious}">
                    <a class="btn btn-secondary" href="<c:url value='/appointments/waitlist?page=${pageSlice.previousPage}&status=${selectedStatus}'/>">Previous</a>
                </c:if>
                <span style="align-self:center; color:var(--text-muted);">Page ${pageSlice.currentPage} of ${pageSlice.totalPages}</span>
                <c:if test="${pageSlice.hasNext}">
                    <a class="btn btn-secondary" href="<c:url value='/appointments/waitlist?page=${pageSlice.nextPage}&status=${selectedStatus}'/>">Next</a>
                </c:if>
            </div>
        </c:if>
    </div>
</div>

<jsp:include page="../layout/footer.jsp" />
