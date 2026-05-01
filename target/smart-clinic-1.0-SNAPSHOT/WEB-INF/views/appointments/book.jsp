<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../layout/header.jsp" />

<div class="breadcrumb-container">
    <a href="<c:url value='/patients/search'/>" class="btn btn-secondary" style="padding: 0.4rem 0.8rem; font-size: 0.875rem;">
        <svg class="icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="m15 18-6-6 6-6"/></svg>
        Back to Patients
    </a>
</div>

<div class="card" style="max-width: 600px; margin: 0 auto;">
    <h2>Book Appointment</h2>
    <p style="color:var(--text-muted); margin-bottom:2rem;">Schedule a consultation with a specialist.</p>
    
    <form action="<c:url value='/appointments/book'/>" method="POST">
        <div class="form-group">
            <label>Patient</label>
            <c:choose>
                <c:when test="${not empty patient}">
                    <div style="background:#F1F5F9; padding:0.75rem; border-radius:8px; border:1px solid var(--border); font-weight:600;">
                        ${patient.name} (#PAT-${patient.id})
                    </div>
                    <input type="hidden" name="patientId" value="${patient.id}"/>
                </c:when>
                <c:otherwise>
                    <select name="patientId" class="form-control" required>
                        <option value="">-- Select Patient --</option>
                        <c:forEach var="p" items="${patients}">
                            <option value="${p.id}">${p.name} (${p.phone})</option>
                        </c:forEach>
                    </select>
                </c:otherwise>
            </c:choose>
        </div>
        
        <div class="form-group">
            <label>Doctor</label>
            <select name="doctorId" id="doctorSelect" class="form-control" required>
                <option value="">-- Select Doctor --</option>
                <c:forEach var="doc" items="${doctors}">
                    <option value="${doc.id}">Dr. ${doc.user.name} - ${doc.specialization}</option>
                </c:forEach>
            </select>
        </div>
        
        <div style="display:grid; grid-template-columns: 1fr 1fr; gap:1.5rem;">
            <div class="form-group">
                <label>Date</label>
                <input type="date" id="dateSelect" class="form-control" required />
            </div>
            <div class="form-group">
                <label>Priority</label>
                <select name="priority" id="prioritySelect" class="form-control" required>
                    <option value="NORMAL">Normal</option>
                    <option value="SENIOR">Senior Citizen</option>
                    <option value="EMERGENCY">Emergency</option>
                </select>
            </div>
        </div>
        
        <div class="form-group">
            <label>Available Slots (Smart Recommended)</label>
            <select name="slotTime" id="slotTimeSelect" class="form-control" required disabled>
                <option value="">Select Date/Doctor first</option>
            </select>
        </div>
        
        <div style="margin-top:2rem; display:flex; gap:1rem;">
            <button type="submit" class="btn btn-primary" style="flex:1">
                <svg class="icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><rect x="3" y="4" width="18" height="18" rx="2" ry="2"/><line x1="16" y1="2" x2="16" y2="6"/><line x1="8" y1="2" x2="8" y2="6"/><line x1="3" y1="10" x2="21" y2="10"/></svg>
                Confirm Booking
            </button>
            <a href="<c:url value='/patients/search'/>" class="btn btn-secondary">Cancel</a>
        </div>
    </form>
</div>

<script>
$(document).ready(function() {
    function updateSlots() {
        const docId = $('#doctorSelect').val();
        const date = $('#dateSelect').val();
        const priority = $('#prioritySelect').val();
        
        if (docId && date) {
            $('#slotTimeSelect').prop('disabled', false).html('<option>Checking availability...</option>');
            $.get('<c:url value="/appointments/api/slots"/>', {
                doctorId: docId,
                date: date,
                priority: priority
            }, function(slots) {
                let html = '';
                if (slots.length === 0) {
                    html = '<option value="">No slots available for this date</option>';
                } else {
                    slots.forEach(s => {
                        const time = new Date(s).toLocaleTimeString([], {hour: '2-digit', minute:'2-digit'});
                        html += `<option value="${s}">${time}</option>`;
                    });
                }
                $('#slotTimeSelect').html(html);
            });
        }
    }
    
    $('#doctorSelect, #dateSelect, #prioritySelect').change(updateSlots);
    
    // Initial update if fields are pre-filled
    if ($('#doctorSelect').val() && $('#dateSelect').val()) updateSlots();
});
</script>

<jsp:include page="../layout/footer.jsp" />
