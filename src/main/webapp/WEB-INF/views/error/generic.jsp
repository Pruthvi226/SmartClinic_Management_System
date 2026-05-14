<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../layout/header.jsp" />

<div class="card" style="max-width: 640px; margin: 4rem auto; text-align: center;">
    <h1 style="margin-top:0; color:var(--emergency);">Something went wrong</h1>
    <p style="color:var(--text-muted); line-height:1.6;">
        SmartClinic could not complete this request. The event has been logged so the team can review it.
    </p>
    <c:if test="${not empty errorMessage}">
        <div style="margin:1.5rem 0; padding:1rem; border-radius:8px; background:#FEF2F2; color:#991B1B; text-align:left;">
            ${errorMessage}
        </div>
    </c:if>
    <a href="<c:url value='/dashboard'/>" class="btn btn-primary">Back to Dashboard</a>
</div>

<jsp:include page="../layout/footer.jsp" />
