<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="../layout/header.jsp" />

<div class="card">
    <div style="display:flex; justify-content:space-between; align-items:center;">
        <h2 style="margin:0">Live Appointment Queue</h2>
        <div style="display:flex; gap:0.5rem;">
            <a href="<c:url value='/appointments/waitlist'/>" class="btn btn-secondary">Waitlist</a>
            <a href="<c:url value='/appointments/book'/>" class="btn btn-primary">
                <svg class="icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M12 5v14M5 12h14"/></svg>
                Book New
            </a>
        </div>
    </div>

    <div style="margin-top: 1.5rem; display:flex; gap:10px; align-items:flex-end;">
        <div style="flex:1">
            <label style="display:block; font-size:0.8rem; margin-bottom:0.4rem; color:var(--text-muted)">Filter by Doctor</label>
            <select id="doctorFilter" class="form-control">
                <option value="0">-- All Doctors --</option>
                <c:forEach var="d" items="${doctors}">
                    <option value="${d.id}">Dr. ${d.user.name} (${d.specialization})</option>
                </c:forEach>
            </select>
        </div>
        <button id="refreshBtn" class="btn btn-secondary">
            <svg class="icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M21 12a9 9 0 1 1-9-9c2.52 0 4.93 1 6.74 2.74L21 8"/><path d="M21 3v5h-5"/></svg>
            Refresh
        </button>
    </div>

    <c:if test="${param.rescheduled != null}">
        <div style="color:#16A34A; background:#F0FDF4; border:1px solid #DCFCE7; padding:1rem; border-radius:8px; margin-top:1.5rem;">
            Appointment rescheduled successfully.
        </div>
    </c:if>
    <c:if test="${param.cancelled != null}">
        <div style="color:#991B1B; background:#FEF2F2; border:1px solid #FECACA; padding:1rem; border-radius:8px; margin-top:1.5rem;">
            Appointment cancelled and removed from the active queue.
        </div>
    </c:if>
    <c:if test="${param.waitlistNotified != null}">
        <div style="color:#92400E; background:#FFFBEB; border:1px solid #FDE68A; padding:1rem; border-radius:8px; margin-top:1.5rem;">
            A matching waitlist patient was marked as notified for the released slot.
        </div>
    </c:if>
    <c:if test="${param.error != null}">
        <div style="color:#991B1B; background:#FEF2F2; border:1px solid #FECACA; padding:1rem; border-radius:8px; margin-top:1.5rem;">
            Could not update that appointment. Refresh the queue and try again.
        </div>
    </c:if>

    <div id="queueContent" style="margin-top: 2rem;">
        <div style="text-align:center; padding:3rem; color:var(--text-muted);">
            Select a doctor to view their current queue
        </div>
    </div>
</div>

<script>
$(document).ready(function() {
    const csrfParam = '${_csrf.parameterName}';
    const csrfToken = '${_csrf.token}';

    window.captureCancelReason = function(form) {
        const reason = prompt('Cancellation reason (optional)');
        if (reason === null) return false;
        form.querySelector('input[name="reason"]').value = reason;
        return confirm('Cancel this appointment?');
    };

    function loadQueue(doctorId) {
        if(doctorId == "0") {
            $("#queueContent").html('<div style="text-align:center; padding:3rem; color:var(--text-muted);">Please select a specific doctor to see their live queue.</div>');
            return;
        }
        
        $("#queueContent").html('<div style="text-align:center; padding:3rem;">Loading live queue...</div>');
        
        $.get("<c:url value='/api/appointments/queue/'/>" + doctorId, function(response) {
            const data = response.data || response;
            if(data.length === 0) {
                $("#queueContent").html('<div style="text-align:center; padding:3rem; color:var(--text-muted);">No appointments scheduled for this doctor today.</div>');
                return;
            }
            
            let html = '<table><thead><tr><th>Time</th><th>Patient</th><th>Priority</th><th>Status</th><th>Actions</th></tr></thead><tbody>';
            data.forEach(function(apt) {
                let badgeClass = 'badge-normal';
                if(apt.priority === 'EMERGENCY') badgeClass = 'badge-emergency';
                if(apt.priority === 'SENIOR') badgeClass = 'badge-senior';
                
                let date = new Date(apt.slotDatetime);
                let timeStr = date.toLocaleTimeString([], {hour: '2-digit', minute:'2-digit'});
                
                const rescheduleUrl = '<c:url value="/appointments/"/>' + apt.id + '/reschedule';
                const cancelUrl = '<c:url value="/appointments/"/>' + apt.id + '/cancel';
                const timelineUrl = '<c:url value="/appointments/"/>' + apt.id + '/timeline';
                const action = apt.status === 'SCHEDULED'
                    ? '<div style="display:flex; gap:0.4rem; align-items:center;">'
                        + '<a href="' + timelineUrl + '" class="btn btn-secondary" style="padding:0.3rem 0.6rem; font-size:0.8rem;">Timeline</a>'
                        + '<a href="' + rescheduleUrl + '" class="btn btn-secondary" style="padding:0.3rem 0.6rem; font-size:0.8rem;">Reschedule</a>'
                        + '<form method="post" action="' + cancelUrl + '" onsubmit="return captureCancelReason(this);" style="display:inline; margin:0;">'
                        + '<input type="hidden" name="' + csrfParam + '" value="' + csrfToken + '"/>'
                        + '<input type="hidden" name="reason" value=""/>'
                        + '<button type="submit" class="btn" style="background:#FEE2E2;color:#991B1B;padding:0.3rem 0.6rem;font-size:0.8rem;">Cancel</button>'
                        + '</form>'
                        + '</div>'
                    : '<a href="' + timelineUrl + '" class="btn btn-secondary" style="padding:0.3rem 0.6rem; font-size:0.8rem;">Timeline</a>';
                html += '<tr>'
                    + '<td><strong>' + timeStr + '</strong></td>'
                    + '<td>' + (apt.patient ? apt.patient.name : 'Unknown') + '</td>'
                    + '<td><span class="badge ' + badgeClass + '">' + apt.priority + '</span></td>'
                    + '<td>' + apt.status + '</td>'
                    + '<td>' + action + '</td>'
                    + '</tr>';
            });
            html += '</tbody></table>';
            $("#queueContent").html(html);
        });
    }

    $("#doctorFilter").change(function() {
        loadQueue($(this).val());
    });

    $("#refreshBtn").click(function() {
        loadQueue($("#doctorFilter").val());
    });
});
</script>

<jsp:include page="../layout/footer.jsp" />
