<%@ page contentType="text/html;charset=UTF-8" language="java" import="java.time.LocalDate" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="../layout/header.jsp" />

<div class="breadcrumb-container">
    <a href="<c:url value='/appointments/queue'/>" class="btn btn-secondary" style="padding: 0.4rem 0.8rem; font-size: 0.875rem;">
        <svg class="icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="m15 18-6-6 6-6"/></svg>
        Back to Queue
    </a>
</div>

<div class="card" style="max-width: 680px; margin: 0 auto;">
    <h2>Reschedule Appointment</h2>
    <p style="color:var(--text-muted); margin-bottom:2rem;">Move the appointment to another doctor slot while keeping the same patient record.</p>

    <div style="background:#F8FAFC; border:1px solid var(--border); border-radius:8px; padding:1rem; margin-bottom:1.5rem;">
        <strong>${appointment.patient.name}</strong><br>
        <span style="color:var(--text-muted); font-size:0.9rem;">
            Current: Dr. ${appointment.doctor.user.name},
            <fmt:formatDate value="${appointment.slotDatetimeAsDate}" type="both" pattern="dd MMM yyyy, HH:mm" />
        </span>
    </div>

    <c:if test="${param.error != null}">
        <div style="color:var(--emergency); background:#FEE2E2; padding:1rem; border-radius:8px; margin-bottom:1rem;">
            Could not reschedule. Select a valid slot and try again.
        </div>
    </c:if>

    <form action="<c:url value='/appointments/${appointment.id}/reschedule'/>" method="POST">
        <div class="form-group">
            <label>Doctor</label>
            <select name="doctorId" id="doctorSelect" class="form-control" required>
                <c:forEach var="doc" items="${doctors}">
                    <option value="${doc.id}" <c:if test="${doc.id == appointment.doctor.id}">selected</c:if>>
                        Dr. ${doc.user.name} - ${doc.specialization}
                    </option>
                </c:forEach>
            </select>
        </div>

        <div style="display:grid; grid-template-columns:1fr 1fr; gap:1.5rem;">
            <div class="form-group">
                <label>New Date</label>
                <input type="date" id="dateSelect" class="form-control" required min="<%= LocalDate.now() %>" />
            </div>
            <div class="form-group">
                <label>Priority</label>
                <select name="priority" id="prioritySelect" class="form-control" required>
                    <option value="NORMAL" <c:if test="${appointment.priority == 'NORMAL'}">selected</c:if>>Normal</option>
                    <option value="SENIOR" <c:if test="${appointment.priority == 'SENIOR'}">selected</c:if>>Senior Citizen</option>
                    <option value="EMERGENCY" <c:if test="${appointment.priority == 'EMERGENCY'}">selected</c:if>>Emergency</option>
                </select>
            </div>
        </div>

        <div class="form-group">
            <label>Available Slots</label>
            <select name="slotTime" id="slotTimeSelect" class="form-control" required disabled>
                <option value="">Select date to load slots</option>
            </select>
        </div>

        <div class="form-group">
            <label>Reschedule Notes</label>
            <textarea name="notes" class="form-control" rows="3" placeholder="Optional notes for reception or doctor">${appointment.notes}</textarea>
        </div>

        <div style="display:flex; gap:1rem; margin-top:2rem;">
            <button type="submit" class="btn btn-primary" style="flex:1;">Save New Slot</button>
            <a href="<c:url value='/appointments/queue'/>" class="btn btn-secondary">Cancel</a>
        </div>
    </form>
</div>

<script>
$(document).ready(function() {
    function updateSlots() {
        const docId = $('#doctorSelect').val();
        const date = $('#dateSelect').val();
        const priority = $('#prioritySelect').val();

        if (docId && date && date.length === 10) {
            $('#slotTimeSelect').prop('disabled', false).html('<option>Checking availability...</option>');
            $.get('<c:url value="/api/appointments/slots"/>', {
                doctorId: docId,
                date: date,
                priority: priority
            }, function(response) {
                const slots = response.data || response;
                let html = '';
                if (slots.length === 0) {
                    html = '<option value="">No slots available for this date</option>';
                } else {
                    slots.forEach(s => {
                        const time = new Date(s).toLocaleTimeString([], {hour: '2-digit', minute:'2-digit'});
                        html += '<option value="' + s + '">' + time + '</option>';
                    });
                }
                $('#slotTimeSelect').html(html);
            });
        }
    }

    $('#doctorSelect, #dateSelect, #prioritySelect').change(updateSlots);
});
</script>

<jsp:include page="../layout/footer.jsp" />
