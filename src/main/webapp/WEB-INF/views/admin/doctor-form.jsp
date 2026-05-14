<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<jsp:include page="../layout/header.jsp" />

<div class="card" style="max-width: 600px; margin: 0 auto;">
    <h2>${formTitle}</h2>
    <p style="color:var(--text-muted); margin-bottom:2rem;">Manage doctor profile details, weekly availability, and slot duration.</p>
    
    <form action="${pageContext.request.contextPath}${formAction}" method="post">
        <div class="form-group">
            <label>Full Name</label>
            <input type="text" name="name" class="form-control" required placeholder="Dr. Name" value="${doctor.user.name}" />
        </div>
        
        <div class="form-group">
            <label>Email Address</label>
            <input type="email" name="email" class="form-control" required placeholder="doctor@smartclinic.com" value="${doctor.user.email}" />
        </div>

        <div class="form-group">
            <label><c:choose><c:when test="${editMode}">New Password</c:when><c:otherwise>Initial Password</c:otherwise></c:choose></label>
            <c:choose>
                <c:when test="${editMode}">
                    <input type="password" name="password" class="form-control" placeholder="Leave blank to keep current password" />
                </c:when>
                <c:otherwise>
                    <input type="password" name="password" class="form-control" required placeholder="Min 6 characters" />
                </c:otherwise>
            </c:choose>
        </div>

        <div class="form-group">
            <label>Specialization</label>
            <input type="text" name="specialization" class="form-control" required placeholder="e.g. Cardiologist" value="${doctor.specialization}" />
        </div>

        <div class="form-group">
            <label>Available Days (Comma separated)</label>
            <input type="text" name="availableDays" class="form-control" required value="${empty doctor.availableDays ? 'MONDAY,TUESDAY,WEDNESDAY,THURSDAY,FRIDAY' : doctor.availableDays}" />
        </div>

        <div class="form-group">
            <label>Slot Duration (Minutes)</label>
            <select name="slotDurationMins" class="form-control">
                <option value="15" <c:if test="${doctor.slotDurationMins == 15}">selected</c:if>>15 mins</option>
                <option value="30" <c:if test="${empty doctor.slotDurationMins || doctor.slotDurationMins == 30}">selected</c:if>>30 mins</option>
                <option value="45" <c:if test="${doctor.slotDurationMins == 45}">selected</c:if>>45 mins</option>
                <option value="60" <c:if test="${doctor.slotDurationMins == 60}">selected</c:if>>60 mins</option>
            </select>
        </div>

        <div style="margin-top: 2rem; display: flex; gap: 1rem;">
            <button type="submit" class="btn btn-primary">${submitLabel}</button>
            <a href="<c:url value='/admin/doctors'/>" class="btn btn-secondary">Cancel</a>
        </div>
    </form>
</div>

<jsp:include page="../layout/footer.jsp" />
