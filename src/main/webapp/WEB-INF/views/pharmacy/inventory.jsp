<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../layout/header.jsp" />

<div class="breadcrumb-container">
    <a href="<c:url value='/pharmacy/queue'/>" class="btn btn-secondary" style="padding:0.4rem 0.8rem; font-size:0.875rem;">Back to Pharmacy Queue</a>
    <a href="<c:url value='/pharmacy/movements'/>" class="btn btn-secondary" style="padding:0.4rem 0.8rem; font-size:0.875rem;">Stock Movements</a>
    <a href="<c:url value='/pharmacy/purchase-order'/>" class="btn btn-secondary" style="padding:0.4rem 0.8rem; font-size:0.875rem;">Purchase Order Draft</a>
</div>

<div style="display:grid; grid-template-columns:360px 1fr; gap:1.5rem; align-items:start;">
    <div class="card" style="margin-bottom:0;">
        <h2 style="margin-top:0;">Medicine Stock</h2>
        <p style="color:var(--text-muted); margin-bottom:1.5rem;">Add medicines or update stock records by medicine name.</p>

        <form action="<c:url value='/pharmacy/inventory'/>" method="post">
            <div class="form-group"><label>Medicine Name</label><input type="text" name="medicineName" class="form-control" required placeholder="Aspirin" /></div>
            <div class="form-group"><label>Category</label><input type="text" name="category" class="form-control" placeholder="Cardiology" /></div>
            <div class="form-group"><label>Current Stock</label><input type="number" min="0" name="stockQuantity" class="form-control" required value="0" /></div>
            <div class="form-group"><label>Reorder Level</label><input type="number" min="0" name="reorderLevel" class="form-control" required value="10" /></div>
            <div class="form-group"><label>Unit Price</label><input type="number" min="0" step="0.01" name="unitPrice" class="form-control" value="0.00" /></div>
            <div class="form-group"><label>Batch Number</label><input type="text" name="batchNumber" class="form-control" placeholder="BATCH-2026-A" /></div>
            <div class="form-group"><label>Expiry Date</label><input type="date" name="expiryDate" class="form-control" /></div>
            <div class="form-group"><label>Supplier</label><input type="text" name="supplierName" class="form-control" placeholder="Supplier name" /></div>
            <div class="form-group"><label>Substitution Suggestion</label><input type="text" name="substitutionName" class="form-control" placeholder="Alternative medicine" /></div>
            <button type="submit" class="btn btn-primary" style="width:100%;">Save Medicine</button>
        </form>
    </div>

    <div class="card" style="margin-bottom:0;">
        <div style="display:flex; justify-content:space-between; align-items:center; gap:1rem;">
            <div>
                <h2 style="margin:0;">Inventory Dashboard</h2>
                <p style="color:var(--text-muted); margin:0.4rem 0 0;">Stock, expiry, supplier and substitution tracking.</p>
            </div>
            <form action="<c:url value='/pharmacy/inventory'/>" method="get" style="display:flex; gap:0.5rem; margin:0;">
                <input type="text" name="keyword" class="form-control" placeholder="Search medicine" value="${keyword}" />
                <button type="submit" class="btn btn-secondary">Search</button>
            </form>
        </div>

        <c:if test="${param.saved != null}">
            <div style="color:#16A34A; background:#F0FDF4; border:1px solid #DCFCE7; padding:0.8rem; border-radius:8px; margin-top:1rem;">Medicine saved.</div>
        </c:if>
        <c:if test="${param.restocked != null}">
            <div style="color:#16A34A; background:#F0FDF4; border:1px solid #DCFCE7; padding:0.8rem; border-radius:8px; margin-top:1rem;">Stock updated.</div>
        </c:if>
        <c:if test="${not empty lowStockMedicines}">
            <div style="color:#92400E; background:#FFFBEB; border:1px solid #FDE68A; padding:1rem; border-radius:8px; margin-top:1.5rem;">
                <strong>Low stock:</strong>
                <c:forEach var="m" items="${lowStockMedicines}" varStatus="loop">${m.medicineName}<c:if test="${!loop.last}">, </c:if></c:forEach>
            </div>
        </c:if>
        <c:if test="${not empty expiringSoonMedicines}">
            <div style="color:#991B1B; background:#FEF2F2; border:1px solid #FECACA; padding:1rem; border-radius:8px; margin-top:1rem;">
                <strong>Expiry watch:</strong>
                <c:forEach var="m" items="${expiringSoonMedicines}" varStatus="loop">${m.medicineName}<c:if test="${!loop.last}">, </c:if></c:forEach>
            </div>
        </c:if>

        <table style="margin-top:1.5rem;">
            <thead>
                <tr><th>Medicine</th><th>Stock</th><th>Price</th><th>Batch/Expiry</th><th>Supplier</th><th>Substitute</th><th>Restock</th></tr>
            </thead>
            <tbody>
                <c:forEach var="m" items="${medicines}">
                    <tr>
                        <td>
                            <strong>${m.medicineName}</strong>
                            <div style="font-size:0.75rem; color:var(--text-muted);">${m.category}</div>
                            <c:if test="${m.lowStock}"><span class="badge" style="background:#FEE2E2; color:#991B1B;">Low</span></c:if>
                        </td>
                        <td>${m.stockQuantity} / ${m.reorderLevel}</td>
                        <td>INR ${m.unitPrice}</td>
                        <td>
                            ${m.batchNumber}
                            <c:if test="${not empty m.expiryDate}">
                                <div style="font-size:0.75rem; color:${m.expiringSoon ? '#991B1B' : 'var(--text-muted)'};">Exp: ${m.expiryDate}</div>
                            </c:if>
                        </td>
                        <td>${m.supplierName}</td>
                        <td>${m.substitutionName}</td>
                        <td>
                            <form action="<c:url value='/pharmacy/inventory/${m.id}/restock'/>" method="post" style="display:flex; gap:0.4rem; margin:0;">
                                <input type="number" min="1" name="quantityToAdd" class="form-control" value="10" style="width:90px;" />
                                <button type="submit" class="btn btn-secondary" style="padding:0.35rem 0.7rem;">Add</button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
                <c:if test="${empty medicines}">
                    <tr><td colspan="7" style="text-align:center; padding:3rem; color:var(--text-muted);">No medicines found.</td></tr>
                </c:if>
            </tbody>
        </table>
    </div>
</div>

<jsp:include page="../layout/footer.jsp" />
