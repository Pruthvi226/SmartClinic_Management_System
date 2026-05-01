<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="../layout/header.jsp" />

<div class="card">
    <div style="display:flex; justify-content:space-between; align-items:center;">
        <h2 style="margin:0">Live Appointment Queue</h2>
        <a href="<c:url value='/appointments/book'/>" class="btn btn-primary">
            <svg class="icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M12 5v14M5 12h14"/></svg>
            Book New
        </a>
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

    <div id="queueContent" style="margin-top: 2rem;">
        <div style="text-align:center; padding:3rem; color:var(--text-muted);">
            Select a doctor to view their current queue
        </div>
    </div>
</div>

<script>
$(document).ready(function() {
    function loadQueue(doctorId) {
        if(doctorId == "0") {
            $("#queueContent").html('<div style="text-align:center; padding:3rem; color:var(--text-muted);">Please select a specific doctor to see their live queue.</div>');
            return;
        }
        
        $("#queueContent").html('<div style="text-align:center; padding:3rem;">Loading live queue...</div>');
        
        $.get("<c:url value='/appointments/api/queue/'/>" + doctorId, function(data) {
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
                
                html += `<tr>
                    <td><strong>${timeStr}</strong></td>
                    <td>${apt.patient.name}</td>
                    <td><span class="badge ${badgeClass}">${apt.priority}</span></td>
                    <td>${apt.status}</td>
                    <td>
                        <a href="<c:url value='/doctor/consult?appointmentId='/>${apt.id}" class="btn btn-secondary" style="padding:0.3rem 0.6rem; font-size:0.8rem;">Start Consult</a>
                    </td>
                </tr>`;
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
