<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<jsp:include page="../layout/header.jsp" />

<div class="breadcrumb-container">
    <a href="<c:url value='/patients/search'/>" class="btn btn-secondary" style="padding: 0.4rem 0.8rem; font-size: 0.875rem;">
        <svg class="icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="m15 18-6-6 6-6"/></svg>
        Back to Directory
    </a>
</div>

<div class="card" style="max-width: 600px; margin: 0 auto;">
    <h2>Register New Patient</h2>
    <p style="color:var(--text-muted); margin-bottom:2rem;">Fill in the patient's basic information to create a new medical record.</p>

    <form:form action="${pageContext.request.contextPath}/patients/register" method="POST" modelAttribute="patient">
        <div class="form-group">
            <label>Full Name</label>
            <form:input path="name" class="form-control" placeholder="e.g. John Doe" required="true" />
            <form:errors path="name" cssClass="text-danger" />
        </div>

        <div style="display:grid; grid-template-columns: 1fr 1fr; gap:1.5rem;">
            <div class="form-group">
                <label>Age</label>
                <form:input path="age" type="number" class="form-control" placeholder="25" required="true" />
            </div>
            <div class="form-group">
                <label>Gender</label>
                <form:select path="gender" class="form-control" required="true">
                    <form:option value="" label="-- Select --" />
                    <form:option value="MALE" label="Male" />
                    <form:option value="FEMALE" label="Female" />
                    <form:option value="OTHER" label="Other" />
                </form:select>
            </div>
        </div>

        <div class="form-group">
            <label>Phone Number</label>
            <form:input path="phone" class="form-control" placeholder="+91 9876543210" required="true" />
        </div>

        <div class="form-group">
            <label>Blood Group</label>
            <form:select path="bloodGroup" class="form-control" required="true">
                <form:option value="" label="-- Select Blood Group --" />
                <form:option value="A+" label="A+" />
                <form:option value="A-" label="A-" />
                <form:option value="B+" label="B+" />
                <form:option value="B-" label="B-" />
                <form:option value="O+" label="O+" />
                <form:option value="O-" label="O-" />
                <form:option value="AB+" label="AB+" />
                <form:option value="AB-" label="AB-" />
            </form:select>
        </div>

        <div style="margin-top:2rem; display:flex; gap:1rem;">
            <button type="submit" class="btn btn-primary" style="flex:1">
                <svg class="icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M19 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h11l5 5v11a2 2 0 0 1-2 2z"/><polyline points="17 21 17 13 7 13 7 21"/><polyline points="7 3 7 8 15 8"/></svg>
                Complete Registration
            </button>
            <a href="<c:url value='/patients/search'/>" class="btn btn-secondary">Cancel</a>
        </div>
    </form:form>
</div>

<jsp:include page="../layout/footer.jsp" />
