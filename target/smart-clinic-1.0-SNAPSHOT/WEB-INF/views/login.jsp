<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="layout/header.jsp" />

<div style="max-width: 400px; margin: 4rem auto;" class="card">
    <h2 style="text-align: center; color: var(--primary);">Welcome Back</h2>
    <p style="text-align: center; color: var(--text-muted); margin-bottom: 2rem;">Sign in to SmartClinic</p>
    
    <c:if test="${param.error != null}">
        <div style="color: var(--emergency); background: #FEE2E2; padding: 1rem; border-radius: 8px; margin-bottom: 1rem;">
            Invalid username or password.
        </div>
    </c:if>
    <c:if test="${param.logout != null}">
        <div style="color: var(--normal); background: #D1FAE5; padding: 1rem; border-radius: 8px; margin-bottom: 1rem;">
            You have been logged out successfully.
        </div>
    </c:if>

    <form action="<c:url value='/authenticateTheUser'/>" method="POST">
        <div class="form-group">
            <label for="username">Email</label>
            <input type="email" id="username" name="username" class="form-control" required />
        </div>
        <div class="form-group">
            <label for="password">Password</label>
            <input type="password" id="password" name="password" class="form-control" required />
        </div>
        <button type="submit" class="btn btn-primary" style="width: 100%;">Sign In</button>
    </form>
    
    <div style="margin-top: 2rem; font-size: 0.9em; color: #666; background: #f9f9f9; padding: 1rem; border-radius:8px; border: 1px dashed #ccc;">
        <strong>Demo Accounts</strong><br>
        <i>Created automatically on startup:</i><br>
        Admin: admin@smartclinic.com<br>
        Pass: admin123
    </div>
</div>

<jsp:include page="layout/footer.jsp" />
