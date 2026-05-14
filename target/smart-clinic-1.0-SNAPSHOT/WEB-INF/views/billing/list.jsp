<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="../layout/header.jsp" />

<div class="card">
    <div style="display:flex; justify-content:space-between; align-items:center; gap:1rem;">
        <h2 style="margin:0;">Financial Billing Records</h2>
        <form action="<c:url value='/billing/list'/>" method="get" style="display:flex; gap:0.5rem; margin:0;">
            <input type="text" name="keyword" class="form-control" placeholder="Invoice or patient" value="${keyword}" />
            <select name="status" class="form-control">
                <option value="">All Statuses</option>
                <option value="PENDING" <c:if test="${status == 'PENDING'}">selected</c:if>>Pending</option>
                <option value="PARTIAL" <c:if test="${status == 'PARTIAL'}">selected</c:if>>Partial</option>
                <option value="PAID" <c:if test="${status == 'PAID'}">selected</c:if>>Paid</option>
                <option value="REFUNDED" <c:if test="${status == 'REFUNDED'}">selected</c:if>>Refunded</option>
            </select>
            <button type="submit" class="btn btn-secondary">Filter</button>
        </form>
    </div>

    <table style="margin-top:1.5rem;">
        <thead>
            <tr>
                <th>Invoice No</th>
                <th>Date</th>
                <th>Patient Name</th>
                <th>Total Amount</th>
                <th>Paid</th>
                <th>Mode</th>
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
                <td><strong>₹${bill.total}</strong></td>
                <td>₹${bill.paidAmount}</td>
                <td>${bill.paymentMode}</td>
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
            <tr><td colspan="8">No billing records found.</td></tr>
            </c:if>
        </tbody>
    </table>

    <c:if test="${pageSlice.totalPages > 1}">
        <div style="display:flex; justify-content:flex-end; gap:0.5rem; margin-top:1rem;">
            <c:if test="${pageSlice.hasPrevious}">
                <a class="btn btn-secondary" href="<c:url value='/billing/list?page=${pageSlice.previousPage}&keyword=${keyword}&status=${status}'/>">Previous</a>
            </c:if>
            <span style="align-self:center; color:var(--text-muted);">Page ${pageSlice.currentPage} of ${pageSlice.totalPages}</span>
            <c:if test="${pageSlice.hasNext}">
                <a class="btn btn-secondary" href="<c:url value='/billing/list?page=${pageSlice.nextPage}&keyword=${keyword}&status=${status}'/>">Next</a>
            </c:if>
        </div>
    </c:if>
</div>

<jsp:include page="../layout/footer.jsp" />
