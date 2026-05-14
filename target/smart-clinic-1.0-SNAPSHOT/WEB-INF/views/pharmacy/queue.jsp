<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="../layout/header.jsp" />

<div class="breadcrumb-container">
    <a href="<c:url value='/dashboard'/>" class="btn btn-secondary" style="padding: 0.4rem 0.8rem; font-size: 0.875rem;">
        <svg class="icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="m15 18-6-6 6-6"/></svg>
        Back to Dashboard
    </a>
</div>

<div class="card">
    <div style="display:flex; justify-content:space-between; align-items:center;">
        <h2 style="margin:0">Pharmacy Dispensation Queue</h2>
        <a href="<c:url value='/pharmacy/inventory'/>" class="btn btn-secondary">
            <svg class="icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M21 16V8a2 2 0 0 0-1-1.73l-7-4a2 2 0 0 0-2 0l-7 4A2 2 0 0 0 3 8v8a2 2 0 0 0 1 1.73l7 4a2 2 0 0 0 2 0l7-4A2 2 0 0 0 21 16Z"/><path d="M3.3 7 12 12l8.7-5"/><path d="M12 22V12"/></svg>
            Inventory
        </a>
    </div>

    <c:if test="${param.dispensed != null}">
        <div style="color:#16A34A; background:#F0FDF4; border:1px solid #DCFCE7; padding:1rem; border-radius:8px; margin-top:1.5rem;">
            Prescription dispensed and inventory stock updated.
        </div>
    </c:if>
    <c:if test="${param.error != null}">
        <div style="color:#991B1B; background:#FEF2F2; border:1px solid #FECACA; padding:1rem; border-radius:8px; margin-top:1.5rem;">
            Could not dispense. Check inventory stock and medicine names, then try again.
        </div>
    </c:if>
    <c:if test="${not empty lowStockMedicines}">
        <div style="color:#92400E; background:#FFFBEB; border:1px solid #FDE68A; padding:1rem; border-radius:8px; margin-top:1.5rem;">
            <strong>${lowStockMedicines.size()} medicine(s) below reorder level.</strong>
            <a href="<c:url value='/pharmacy/inventory'/>" style="color:#92400E; font-weight:700;">Review inventory</a>
        </div>
    </c:if>

    <table>
        <thead>
            <tr>
                <th>Prescription ID</th>
                <th>Patient Name</th>
                <th>Doctor</th>
                <th>Issued At</th>
                <th>Medicines</th>
                <th>Action</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="p" items="${prescriptions}">
            <tr>
                <td><strong style="color:var(--text-muted);">#PRE-${p.id}</strong></td>
                <td>
                    <strong>${p.patient.name}</strong>
                    <c:if test="${not empty p.patient.allergies && p.patient.allergies != 'None'}">
                        <div style="color:#991B1B; font-size:0.75rem; font-weight:700;">Allergies: ${p.patient.allergies}</div>
                    </c:if>
                </td>
                <td>Dr. ${p.doctor.user.name}</td>
                <td><fmt:formatDate value="${p.issuedAt}" type="both" pattern="dd MMM yyyy, HH:mm"/></td>
                <td>
                    <c:forEach var="item" items="${p.items}">
                        <div style="font-size:0.875rem; margin-bottom:0.35rem;">
                            <strong>${item.medicineName}</strong>
                            <span style="color:var(--text-muted);">x ${item.quantity} - ${item.dosage}, ${item.duration}</span>
                        </div>
                    </c:forEach>
                </td>
                <td>
                    <form action="<c:url value='/pharmacy/prescriptions/${p.id}/dispense'/>" method="post" style="margin:0;" onsubmit="return confirm('Dispense this prescription and reduce stock?');">
                        <button type="submit" class="btn btn-primary" style="padding:0.4rem 0.8rem; font-size:0.8rem;">
                        <svg class="icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="m10.5 20.5 10-10a4.95 4.95 0 1 0-7-7l-10 10a4.95 4.95 0 1 0 7 7Z"/><path d="m8.5 8.5 7 7"/></svg>
                            Dispense
                        </button>
                    </form>
                    <a href="<c:url value='/prescriptions/download/${p.id}'/>" class="btn btn-secondary" style="padding:0.35rem 0.7rem; font-size:0.8rem; margin-top:0.4rem;">PDF</a>
                </td>
            </tr>
            </c:forEach>
            <c:if test="${empty prescriptions}">
            <tr><td colspan="6" style="text-align:center; padding:3rem; color:var(--text-muted);">No pending prescriptions in the queue.</td></tr>
            </c:if>
        </tbody>
    </table>
</div>

<jsp:include page="../layout/footer.jsp" />
