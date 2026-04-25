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
                <th style="text-align:right;">Amount (USD)</th>
            </tr>
        </thead>
        <tbody>
            <tr>
                <td>Hospital Consultation Fee (Dr. ${bill.appointment.doctor.user.name})</td>
                <td style="text-align:right;">$${bill.amount}</td>
            </tr>
            <tr>
                <td>Tax Calculation (18% from MySQL Proc)</td>
                <td style="text-align:right;">$${bill.tax}</td>
            </tr>
            <tr>
                <td style="text-align:right; font-size:1.2rem; border-top:2px solid #000;"><strong>Total Due:</strong></td>
                <td style="text-align:right; font-size:1.2rem; border-top:2px solid #000; color:var(--primary);"><strong>$${bill.total}</strong></td>
            </tr>
        </tbody>
    </table>
    
    <div style="margin-top: 3rem; text-align:center;">
        Status: <span class="badge ${bill.paymentStatus == 'PENDING' ? 'badge-senior' : 'badge-normal'}">${bill.paymentStatus}</span><br><br>
        <a href="<c:url value='/billing/download/${bill.id}'/>" class="btn btn-primary">Download Formal PDF</a>
    </div>
</div>

<jsp:include page="../layout/footer.jsp" />
