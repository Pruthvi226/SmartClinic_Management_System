<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../layout/header.jsp" />

<div class="card" style="max-width: 600px; margin: 4rem auto; text-align: center; padding: 4rem 2rem;">
    <div style="background: #FEE2E2; color: #DC2626; width: 80px; height: 80px; border-radius: 50%; display: flex; align-items: center; justify-content: center; margin: 0 auto 2rem;">
        <svg class="icon" style="width: 40px; height: 40px;" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-3L13.732 4c-.77-1.333-2.694-1.333-3.464 0L3.34 16c-.77 1.333.192 3 1.732 3z"/></svg>
    </div>
    <h1 style="font-size: 2rem; margin-bottom: 1rem;">Access Denied</h1>
    <p style="color: var(--text-muted); font-size: 1.1rem; margin-bottom: 2rem;">
        You do not have the necessary permissions to view this page. If you believe this is an error, please contact the administrator.
    </p>
    <a href="<c:url value='/dashboard'/>" class="btn btn-primary" style="padding: 0.75rem 2rem;">
        Go to Dashboard
    </a>
</div>

<jsp:include page="../layout/footer.jsp" />
