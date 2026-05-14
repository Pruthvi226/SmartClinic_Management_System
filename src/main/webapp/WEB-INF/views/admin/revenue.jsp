<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="../layout/header.jsp" />

<div class="card">
    <div style="display:flex; justify-content:space-between; align-items:center; gap:1rem;">
        <h2 style="margin:0;">Revenue Analytics</h2>
        <form action="<c:url value='/admin/revenue'/>" method="get" style="display:flex; gap:0.5rem; margin:0;">
            <input type="date" name="from" class="form-control" value="${from}" />
            <input type="date" name="to" class="form-control" value="${to}" />
            <button class="btn btn-secondary">Run</button>
        </form>
    </div>

    <div style="display:grid; grid-template-columns: repeat(4, 1fr); gap:1rem; margin-top:1.5rem;">
        <div style="background:#F0FDF4; border:1px solid #BBF7D0; border-radius:8px; padding:1rem;"><div>Collected</div><strong style="font-size:1.8rem;">INR ${paidRevenue}</strong></div>
        <div style="background:#EEF2FF; border:1px solid #C7D2FE; border-radius:8px; padding:1rem;"><div>Discounts</div><strong style="font-size:1.8rem;">INR ${discounts}</strong></div>
        <div style="background:#FEF2F2; border:1px solid #FECACA; border-radius:8px; padding:1rem;"><div>Refunds</div><strong style="font-size:1.8rem;">INR ${refunds}</strong></div>
        <div style="background:#ECFEFF; border:1px solid #A5F3FC; border-radius:8px; padding:1rem;"><div>Insurance Claims</div><strong style="font-size:1.8rem;">${insuranceClaims}</strong></div>
    </div>

    <div style="margin-top:1rem;">
        <a href="<c:url value='/admin/export/patients'/>" class="btn btn-secondary">Export Patients CSV</a>
        <a href="<c:url value='/admin/export/billing'/>" class="btn btn-secondary">Export Billing CSV</a>
        <a href="<c:url value='/admin/export/audit'/>" class="btn btn-secondary">Export Audit CSV</a>
    </div>

    <table style="margin-top:1.5rem;">
        <thead><tr><th>Invoice</th><th>Date</th><th>Patient</th><th>Paid</th><th>Discount</th><th>Refund</th><th>Insurance</th></tr></thead>
        <tbody>
            <c:forEach var="bill" items="${bills}">
                <tr>
                    <td>INV-${bill.id}</td>
                    <td><fmt:formatDate value="${bill.generatedAt}" type="date" pattern="dd MMM yyyy"/></td>
                    <td>${bill.appointment.patient.name}</td>
                    <td>INR ${bill.paidAmount}</td>
                    <td>INR ${bill.discountAmount}</td>
                    <td>INR ${bill.refundAmount}</td>
                    <td>${bill.insuranceStatus}</td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</div>

<jsp:include page="../layout/footer.jsp" />
