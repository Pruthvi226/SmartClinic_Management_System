<%@ page contentType="text/html;charset=UTF-8" language="java" import="java.time.LocalDate" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../layout/header.jsp" />

<div class="breadcrumb-container">
    <a href="<c:url value='/admin/doctors'/>" class="btn btn-secondary" style="padding: 0.4rem 0.8rem; font-size: 0.875rem;">
        <svg class="icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="m15 18-6-6 6-6"/></svg>
        Back to Doctors
    </a>
</div>

<div style="display:grid; grid-template-columns: 360px 1fr; gap:1.5rem; align-items:start;">
    <div class="card" style="margin-bottom:0;">
        <h2 style="margin-top:0;">Block Doctor Leave</h2>
        <p style="color:var(--text-muted);">Dr. ${doctor.user.name} | ${doctor.specialization}</p>

        <c:if test="${param.added != null}">
            <div style="color:#16A34A; background:#F0FDF4; border:1px solid #DCFCE7; padding:0.8rem; border-radius:8px; margin-bottom:1rem;">Leave date added.</div>
        </c:if>
        <c:if test="${param.deleted != null}">
            <div style="color:#16A34A; background:#F0FDF4; border:1px solid #DCFCE7; padding:0.8rem; border-radius:8px; margin-bottom:1rem;">Leave date removed.</div>
        </c:if>

        <form action="<c:url value='/admin/doctors/${doctor.id}/leaves'/>" method="post">
            <div class="form-group">
                <label>Leave Date</label>
                <input type="date" name="leaveDate" class="form-control" required min="<%= LocalDate.now() %>" />
            </div>
            <div class="form-group">
                <label>Reason</label>
                <input type="text" name="reason" class="form-control" placeholder="Conference, holiday, emergency leave" />
            </div>
            <button type="submit" class="btn btn-primary" style="width:100%;">Block Slots</button>
        </form>
    </div>

    <div class="card" style="margin-bottom:0;">
        <h2 style="margin-top:0;">Blocked Dates</h2>
        <table>
            <thead>
                <tr>
                    <th>Date</th>
                    <th>Reason</th>
                    <th>Action</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="leave" items="${leaves}">
                    <tr>
                        <td><strong>${leave.leaveDate}</strong></td>
                        <td>${leave.reason}</td>
                        <td>
                            <form action="<c:url value='/admin/doctors/${doctor.id}/leaves/${leave.id}/delete'/>" method="post" style="margin:0;">
                                <button type="submit" class="btn" style="background:#FEE2E2;color:#991B1B;padding:0.35rem 0.7rem;">Remove</button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
                <c:if test="${empty leaves}">
                    <tr><td colspan="3" style="text-align:center; padding:2rem; color:var(--text-muted);">No leave dates blocked.</td></tr>
                </c:if>
            </tbody>
        </table>
    </div>
</div>

<jsp:include page="../layout/footer.jsp" />
