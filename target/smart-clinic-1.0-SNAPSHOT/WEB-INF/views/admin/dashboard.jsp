<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../layout/header.jsp" />

<div class="card">
    <h2>Admin Dashboard</h2>
    
    <div style="display:grid; grid-template-columns: repeat(3, 1fr); gap: 20px; margin-top: 2rem;">
        <div style="background: linear-gradient(135deg, #4F46E5 0%, #3b82f6 100%); color:white; padding: 2rem; border-radius:16px;">
            <h3 style="margin:0; font-weight:400; font-size:1.1rem;">Total Patients</h3>
            <p style="font-size:3rem; font-weight:700; margin:0.5rem 0 0 0;">${totalPatients}</p>
        </div>
        <div style="background: linear-gradient(135deg, #10B981 0%, #059669 100%); color:white; padding: 2rem; border-radius:16px;">
            <h3 style="margin:0; font-weight:400; font-size:1.1rem;">System Health</h3>
            <p style="font-size:3rem; font-weight:700; margin:0.5rem 0 0 0;">100%</p>
        </div>
        <div style="background: linear-gradient(135deg, #F59E0B 0%, #D97706 100%); color:white; padding: 2rem; border-radius:16px;">
            <h3 style="margin:0; font-weight:400; font-size:1.1rem;">Active Modules</h3>
            <p style="font-size:3rem; font-weight:700; margin:0.5rem 0 0 0;">4/4</p>
        </div>
    </div>
    
    <div style="margin-top:2rem;">
        <h3>Quick Links</h3>
        <a href="<c:url value='/admin/audit-log'/>" class="btn btn-primary" style="margin-right:1rem;">View Security Audit Logs</a>
        <a href="<c:url value='/billing/list'/>" class="btn" style="background:#10B981;color:white;">View Hospital Revenue / Bills</a>
    </div>
</div>

<jsp:include page="../layout/footer.jsp" />
