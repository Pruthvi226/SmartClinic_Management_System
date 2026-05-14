<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../layout/header.jsp" />

<div class="card" style="max-width:800px; margin:0 auto;">
    <div style="display:flex; justify-content:space-between; align-items:center;">
        <div>
            <h2 style="margin:0;">Doctor Profile</h2>
            <p style="color:var(--text-muted); margin:0.4rem 0 0;">Clinical identity, department, and availability shown to reception.</p>
        </div>
        <a href="<c:url value='/doctor/schedule'/>" class="btn btn-secondary">Back to Schedule</a>
    </div>

    <div style="display:grid; grid-template-columns: repeat(2, 1fr); gap:1rem; margin-top:1.5rem;">
        <div style="background:#F8FAFC; border:1px solid var(--border); border-radius:8px; padding:1rem;">
            <div style="color:var(--text-muted);">Name</div>
            <strong>Dr. ${doctor.user.name}</strong>
        </div>
        <div style="background:#F8FAFC; border:1px solid var(--border); border-radius:8px; padding:1rem;">
            <div style="color:var(--text-muted);">Email</div>
            <strong>${doctor.user.email}</strong>
        </div>
        <div style="background:#F8FAFC; border:1px solid var(--border); border-radius:8px; padding:1rem;">
            <div style="color:var(--text-muted);">Specialization</div>
            <strong>${doctor.specialization}</strong>
        </div>
        <div style="background:#F8FAFC; border:1px solid var(--border); border-radius:8px; padding:1rem;">
            <div style="color:var(--text-muted);">Slot Duration</div>
            <strong>${doctor.slotDurationMins} minutes</strong>
        </div>
    </div>

    <div style="margin-top:1.5rem; background:#EEF2FF; border:1px solid #C7D2FE; border-radius:8px; padding:1rem;">
        <div style="color:var(--text-muted);">Available Days</div>
        <strong>${doctor.availableDays}</strong>
    </div>
</div>

<jsp:include page="../layout/footer.jsp" />
