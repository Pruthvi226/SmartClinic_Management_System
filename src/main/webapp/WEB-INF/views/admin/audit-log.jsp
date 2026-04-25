<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="../layout/header.jsp" />

<div class="card">
    <h2>Security Audit Log</h2>
    
    <table style="font-size:0.9rem;">
        <thead>
            <tr>
                <th>ID</th>
                <th>Timestamp</th>
                <th>User Access</th>
                <th>Action</th>
                <th>IP Address</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="log" items="${logs}">
            <tr>
                <td>${log.id}</td>
                <td><fmt:formatDate value="${log.timestamp}" type="both" pattern="dd MMM yyyy, HH:mm:ss"/></td>
                <td>${log.user != null ? log.user.email : 'Anonymous / System'}</td>
                <td><code>${log.action}</code></td>
                <td>${log.ipAddress}</td>
            </tr>
            </c:forEach>
            <c:if test="${empty logs}">
            <tr><td colspan="5">No audit logs found.</td></tr>
            </c:if>
        </tbody>
    </table>
</div>

<jsp:include page="../layout/footer.jsp" />
