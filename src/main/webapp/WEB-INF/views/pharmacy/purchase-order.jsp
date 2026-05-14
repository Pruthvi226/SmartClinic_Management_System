<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../layout/header.jsp" />

<div class="card">
    <div style="display:flex; justify-content:space-between; align-items:center;">
        <h2 style="margin:0;">Purchase Order Draft</h2>
        <a href="<c:url value='/pharmacy/inventory'/>" class="btn btn-secondary">Inventory</a>
    </div>
    <p style="color:var(--text-muted);">Draft reorder list based on low stock and expiry watch.</p>

    <table style="margin-top:1.5rem;">
        <thead><tr><th>Medicine</th><th>Supplier</th><th>Current</th><th>Reorder Level</th><th>Suggested Qty</th><th>Reason</th></tr></thead>
        <tbody>
            <c:forEach var="m" items="${lowStockMedicines}">
                <tr><td>${m.medicineName}</td><td>${m.supplierName}</td><td>${m.stockQuantity}</td><td>${m.reorderLevel}</td><td>${m.reorderLevel * 2}</td><td>Below reorder level</td></tr>
            </c:forEach>
            <c:forEach var="m" items="${expiringSoonMedicines}">
                <tr><td>${m.medicineName}</td><td>${m.supplierName}</td><td>${m.stockQuantity}</td><td>${m.reorderLevel}</td><td>${m.reorderLevel}</td><td>Expiry watch: ${m.expiryDate}</td></tr>
            </c:forEach>
            <c:if test="${empty lowStockMedicines && empty expiringSoonMedicines}">
                <tr><td colspan="6" style="text-align:center; padding:3rem; color:var(--text-muted);">No purchase order needed right now.</td></tr>
            </c:if>
        </tbody>
    </table>
</div>

<jsp:include page="../layout/footer.jsp" />
