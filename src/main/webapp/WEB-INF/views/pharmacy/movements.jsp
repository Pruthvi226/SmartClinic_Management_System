<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="../layout/header.jsp" />

<div class="card">
    <div style="display:flex; justify-content:space-between; align-items:center;">
        <h2 style="margin:0;">Stock Movement History</h2>
        <a href="<c:url value='/pharmacy/inventory'/>" class="btn btn-secondary">Inventory</a>
    </div>

    <table style="margin-top:1.5rem;">
        <thead><tr><th>Time</th><th>Medicine</th><th>Type</th><th>Quantity</th><th>Reason</th></tr></thead>
        <tbody>
            <c:forEach var="movement" items="${movements}">
                <tr>
                    <td><fmt:formatDate value="${movement.createdAt}" type="both" pattern="dd MMM yyyy, HH:mm"/></td>
                    <td>${movement.medicine.medicineName}</td>
                    <td><span class="badge badge-normal">${movement.movementType}</span></td>
                    <td>${movement.quantity}</td>
                    <td>${movement.reason}</td>
                </tr>
            </c:forEach>
            <c:if test="${empty movements}">
                <tr><td colspan="5" style="text-align:center; padding:3rem; color:var(--text-muted);">No movement history yet.</td></tr>
            </c:if>
        </tbody>
    </table>
</div>

<jsp:include page="../layout/footer.jsp" />
