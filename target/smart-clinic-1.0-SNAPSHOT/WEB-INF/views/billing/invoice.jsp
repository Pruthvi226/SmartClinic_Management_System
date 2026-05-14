<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="../layout/header.jsp" />

<div class="card" style="max-width:800px; margin:0 auto; padding: 3rem;">
    <div style="display:flex;justify-content:space-between;align-items:flex-start; margin-bottom: 2rem;">
        <div>
            <h1 style="color:var(--primary);margin:0;">SmartClinic</h1>
            <p style="color:var(--text-muted);margin:0;">123 Healthcare Ave, Medical City</p>
        </div>
        <div style="text-align:right;">
            <h2 style="margin:0;color:var(--text-muted);">INVOICE</h2>
            <strong style="font-size:1.2rem;">#INV-${bill.id}</strong><br>
            Date: <fmt:formatDate value="${bill.generatedAt}" type="date" pattern="dd MMM yyyy"/>
        </div>
    </div>
    
    <div style="margin-bottom: 2rem;">
        <strong>Billed To:</strong><br>
        ${bill.appointment.patient.name}<br>
        ${bill.appointment.patient.phone}<br>
        ${bill.appointment.patient.address}
    </div>
    
    <table>
        <thead>
            <tr>
                <th>Description</th>
                <th style="text-align:right;">Amount (INR)</th>
            </tr>
        </thead>
        <tbody>
            <tr>
                <td>Hospital Consultation Fee (Dr. ${bill.appointment.doctor.user.name})</td>
                <td style="text-align:right;">₹${bill.amount}</td>
            </tr>
            <tr>
                <td>Tax Calculation (18% from MySQL Proc)</td>
                <td style="text-align:right;">₹${bill.tax}</td>
            </tr>
            <tr>
                <td style="text-align:right; font-size:1.2rem; border-top:2px solid #000;"><strong>Total Due:</strong></td>
                <td style="text-align:right; font-size:1.2rem; border-top:2px solid #000; color:var(--primary);"><strong>₹${bill.total}</strong></td>
            </tr>
            <tr>
                <td style="text-align:right;"><strong>Paid:</strong></td>
                <td style="text-align:right;">₹${bill.paidAmount}</td>
            </tr>
            <tr>
                <td style="text-align:right;"><strong>Balance:</strong></td>
                <td style="text-align:right;">₹${bill.balanceDue}</td>
            </tr>
        </tbody>
    </table>
    
    <div style="margin-top: 3rem; text-align:center;">
        Status: <span class="badge ${bill.paymentStatus == 'PENDING' ? 'badge-senior' : 'badge-normal'}">${bill.paymentStatus}</span>
        <span style="color:var(--text-muted); margin-left:0.5rem;">Mode: ${bill.paymentMode} | Ref: ${bill.paymentReference}</span><br><br>
        <a href="<c:url value='/billing/download/${bill.id}'/>" class="btn btn-primary">Download Formal PDF</a>
    </div>

    <div style="margin-top:2rem; background:#F8FAFC; border:1px solid var(--border); padding:1rem; border-radius:8px;">
        <h3 style="margin-top:0;">Update Payment</h3>
        <c:if test="${param.paymentUpdated != null}">
            <div style="color:#16A34A; background:#F0FDF4; border:1px solid #DCFCE7; padding:0.8rem; border-radius:8px; margin-bottom:1rem;">Payment updated.</div>
        </c:if>
        <form action="<c:url value='/billing/invoice/${bill.id}/payment'/>" method="post" style="display:grid; grid-template-columns: repeat(5, 1fr); gap:0.8rem; align-items:end;">
            <div class="form-group" style="margin:0;">
                <label>Status</label>
                <select name="status" class="form-control">
                    <option value="PENDING" <c:if test="${bill.paymentStatus == 'PENDING'}">selected</c:if>>Pending</option>
                    <option value="PARTIAL" <c:if test="${bill.paymentStatus == 'PARTIAL'}">selected</c:if>>Partial</option>
                    <option value="PAID" <c:if test="${bill.paymentStatus == 'PAID'}">selected</c:if>>Paid</option>
                    <option value="REFUNDED" <c:if test="${bill.paymentStatus == 'REFUNDED'}">selected</c:if>>Refunded</option>
                </select>
            </div>
            <div class="form-group" style="margin:0;">
                <label>Mode</label>
                <select name="paymentMode" class="form-control">
                    <option value="CASH" <c:if test="${bill.paymentMode == 'CASH'}">selected</c:if>>Cash</option>
                    <option value="CARD" <c:if test="${bill.paymentMode == 'CARD'}">selected</c:if>>Card</option>
                    <option value="UPI" <c:if test="${bill.paymentMode == 'UPI'}">selected</c:if>>UPI</option>
                    <option value="INSURANCE" <c:if test="${bill.paymentMode == 'INSURANCE'}">selected</c:if>>Insurance</option>
                </select>
            </div>
            <div class="form-group" style="margin:0;">
                <label>Paid Amount</label>
                <input type="number" min="0" step="0.01" name="paidAmount" class="form-control" value="${bill.paidAmount}" />
            </div>
            <div class="form-group" style="margin:0;">
                <label>Reference</label>
                <input type="text" name="paymentReference" class="form-control" value="${bill.paymentReference}" />
            </div>
            <button type="submit" class="btn btn-secondary">Save Payment</button>
        </form>
    </div>
</div>

<jsp:include page="../layout/footer.jsp" />
