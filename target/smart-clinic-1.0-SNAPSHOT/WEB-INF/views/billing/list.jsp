<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="../layout/header.jsp" />

<div class="card">
    <h2>Financial Billing Records</h2>

    <table>
        <thead>
            <tr>
                <th>Invoice No</th>
                <th>Date</th>
                <th>Patient Name</th>
                <th>Total Amount</th>
                <th>Status</th>
                <th>Action</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="bill" items="${bills}">
            <tr>
                <td>INV-${bill.id}</td>
                <td><fmt:formatDate value="${bill.generatedAt}" type="both" pattern="dd MMM yyyy"/></td>
                <td>${bill.appointment.patient.name}</td>
                <td><strong>$${bill.total}</strong></td>
                <td>
                    <span class="badge ${bill.paymentStatus == 'PENDING' ? 'badge-senior' : 'badge-normal'}">${bill.paymentStatus}</span>
                </td>
                <td>
                    <a href="<c:url value='/billing/invoice/${bill.id}'/>" class="btn btn-primary" style="padding:0.3rem 0.6rem;font-size:0.875rem;">View</a>
                    <a href="<c:url value='/billing/download/${bill.id}'/>" class="btn" style="background:var(--text-dark);color:white;padding:0.3rem 0.6rem;font-size:0.875rem;">PDF</a>
                </td>
            </tr>
            </c:forEach>
            <c:if test="${empty bills}">
            <tr><td colspan="6">No billing records found.</td></tr>
            </c:if>
        </tbody>
    </table>
</div>

<jsp:include page="../layout/footer.jsp" />
