<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="../layout/header.jsp" />

<div class="card">
    <div style="display:flex; justify-content:space-between; align-items:center; gap:1rem;">
        <div>
            <h2 style="margin:0;">Daily Hospital Report</h2>
            <p style="color:var(--text-muted); margin:0.4rem 0 0;">Appointments, revenue, pending bills, and pharmacy stock for ${reportDate}.</p>
        </div>
        <form action="<c:url value='/admin/reports'/>" method="get" style="display:flex; gap:0.5rem; margin:0;">
            <input type="date" name="date" class="form-control" value="${reportDate}" />
            <button type="submit" class="btn btn-secondary">Run</button>
        </form>
    </div>

    <div style="display:grid; grid-template-columns: repeat(auto-fit, minmax(180px, 1fr)); gap:1rem; margin-top:1.5rem;">
        <div style="background:#EEF2FF; border:1px solid #C7D2FE; border-radius:8px; padding:1rem;">
            <div style="color:var(--text-muted);">Appointments</div>
            <strong style="font-size:2rem;">${appointmentCount}</strong>
        </div>
        <div style="background:#F0FDF4; border:1px solid #BBF7D0; border-radius:8px; padding:1rem;">
            <div style="color:var(--text-muted);">Completed</div>
            <strong style="font-size:2rem;">${completedCount}</strong>
        </div>
        <div style="background:#ECFEFF; border:1px solid #A5F3FC; border-radius:8px; padding:1rem;">
            <div style="color:var(--text-muted);">Collected Revenue</div>
            <strong style="font-size:2rem;">INR ${paidRevenue}</strong>
        </div>
        <div style="background:#FFFBEB; border:1px solid #FDE68A; border-radius:8px; padding:1rem;">
            <div style="color:var(--text-muted);">Low Stock</div>
            <strong style="font-size:2rem;">${lowStockMedicines.size()}</strong>
        </div>
    </div>
</div>

<div style="display:grid; grid-template-columns: 1fr 1fr; gap:1.5rem; align-items:start;">
    <div class="card" style="margin-bottom:0;">
        <h3 style="margin-top:0;">Appointments</h3>
        <table>
            <thead>
                <tr><th>Time</th><th>Patient</th><th>Doctor</th><th>Status</th></tr>
            </thead>
            <tbody>
                <c:forEach var="a" items="${appointments}">
                    <tr>
                        <td><fmt:formatDate value="${a.slotDatetimeAsDate}" type="time" pattern="HH:mm"/></td>
                        <td>${a.patient.name}</td>
                        <td>Dr. ${a.doctor.user.name}</td>
                        <td>${a.status}</td>
                    </tr>
                </c:forEach>
                <c:if test="${empty appointments}">
                    <tr><td colspan="4" style="text-align:center; padding:2rem; color:var(--text-muted);">No appointments for this date.</td></tr>
                </c:if>
            </tbody>
        </table>
    </div>

    <div class="card" style="margin-bottom:0;">
        <h3 style="margin-top:0;">Operational Alerts</h3>
        <h4>Pending Bills</h4>
        <table>
            <thead><tr><th>Invoice</th><th>Patient</th><th>Balance</th></tr></thead>
            <tbody>
                <c:forEach var="bill" items="${pendingBills}">
                    <tr>
                        <td>INV-${bill.id}</td>
                        <td>${bill.appointment.patient.name}</td>
                        <td>INR ${bill.balanceDue}</td>
                    </tr>
                </c:forEach>
                <c:if test="${empty pendingBills}">
                    <tr><td colspan="3" style="text-align:center; color:var(--text-muted);">No pending bills.</td></tr>
                </c:if>
            </tbody>
        </table>

        <h4 style="margin-top:1.5rem;">Low Stock Medicines</h4>
        <table>
            <thead><tr><th>Medicine</th><th>Stock</th><th>Reorder</th></tr></thead>
            <tbody>
                <c:forEach var="m" items="${lowStockMedicines}">
                    <tr>
                        <td>${m.medicineName}</td>
                        <td>${m.stockQuantity}</td>
                        <td>${m.reorderLevel}</td>
                    </tr>
                </c:forEach>
                <c:if test="${empty lowStockMedicines}">
                    <tr><td colspan="3" style="text-align:center; color:var(--text-muted);">Inventory looks healthy.</td></tr>
                </c:if>
            </tbody>
        </table>
    </div>
</div>

<jsp:include page="../layout/footer.jsp" />
