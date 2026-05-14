<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="../layout/header.jsp" />

<div class="card">
    <h2>Security Audit Log</h2>
    <form action="<c:url value='/admin/audit-log'/>" method="get" style="display:grid; grid-template-columns: 1.2fr 1fr 1fr 1fr 1fr auto; gap:0.6rem; align-items:end; margin:1rem 0;">
        <div class="form-group" style="margin:0;">
            <label>Action</label>
            <input type="text" name="keyword" class="form-control" value="${keyword}" placeholder="booked, access, dispense" />
        </div>
        <div class="form-group" style="margin:0;">
            <label>User</label>
            <input type="text" name="user" class="form-control" value="${user}" placeholder="email" />
        </div>
        <div class="form-group" style="margin:0;">
            <label>Entity</label>
            <select name="entityType" class="form-control">
                <option value="">All</option>
                <option value="APPOINTMENT" <c:if test="${entityType == 'APPOINTMENT'}">selected</c:if>>Appointment</option>
                <option value="WAITLIST" <c:if test="${entityType == 'WAITLIST'}">selected</c:if>>Waitlist</option>
                <option value="PATIENT" <c:if test="${entityType == 'PATIENT'}">selected</c:if>>Patient</option>
                <option value="SYSTEM" <c:if test="${entityType == 'SYSTEM'}">selected</c:if>>System</option>
            </select>
        </div>
        <div class="form-group" style="margin:0;">
            <label>From</label>
            <input type="date" name="from" class="form-control" value="${from}" />
        </div>
        <div class="form-group" style="margin:0;">
            <label>To</label>
            <input type="date" name="to" class="form-control" value="${to}" />
        </div>
        <button type="submit" class="btn btn-secondary">Filter</button>
    </form>
    
    <table style="font-size:0.9rem;">
        <thead>
            <tr>
                <th>ID</th>
                <th>Timestamp</th>
                <th>User Access</th>
                <th>Action</th>
                <th>Entity</th>
                <th>IP Address</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="log" items="${logs}">
            <tr>
                <td>${log.id}</td>
                <td><fmt:formatDate value="${log.timestamp}" type="both" pattern="dd MMM yyyy, HH:mm:ss"/></td>
                <td>${log.user != null ? log.user.email : 'Anonymous / System'}</td>
                <td><code>${log.action}</code></td>
                <td>
                    <c:choose>
                        <c:when test="${not empty log.entityType}">${log.entityType} #${log.entityId}</c:when>
                        <c:otherwise>-</c:otherwise>
                    </c:choose>
                </td>
                <td>${log.ipAddress}</td>
            </tr>
            </c:forEach>
            <c:if test="${empty logs}">
            <tr><td colspan="6">No audit logs found.</td></tr>
            </c:if>
        </tbody>
    </table>

    <c:if test="${pageSlice.totalPages > 1}">
        <div style="display:flex; justify-content:flex-end; gap:0.5rem; margin-top:1rem;">
            <c:if test="${pageSlice.hasPrevious}">
                <a class="btn btn-secondary" href="<c:url value='/admin/audit-log?page=${pageSlice.previousPage}&keyword=${keyword}&entityType=${entityType}&user=${user}&from=${from}&to=${to}'/>">Previous</a>
            </c:if>
            <span style="align-self:center; color:var(--text-muted);">Page ${pageSlice.currentPage} of ${pageSlice.totalPages} (${pageSlice.totalItems} logs)</span>
            <c:if test="${pageSlice.hasNext}">
                <a class="btn btn-secondary" href="<c:url value='/admin/audit-log?page=${pageSlice.nextPage}&keyword=${keyword}&entityType=${entityType}&user=${user}&from=${from}&to=${to}'/>">Next</a>
            </c:if>
        </div>
    </c:if>
</div>

<jsp:include page="../layout/footer.jsp" />
