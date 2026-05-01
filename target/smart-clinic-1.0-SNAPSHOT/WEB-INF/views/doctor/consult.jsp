<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../layout/header.jsp" />

<div class="card">
    <h2>Consultation: ${appointment.patient.name}</h2>
    <div style="margin-bottom: 2rem; background: #f9fafb; padding:1rem; border-radius:8px;">
        <strong>Dob:</strong> ${appointment.patient.dob} | 
        <strong>Blood:</strong> ${appointment.patient.bloodGroup} | 
        <strong>Allergies/History:</strong> Check Patient History tab.
    </div>

    <form action="<c:url value='/doctor/consult/${appointment.id}'/>" method="POST">
        <div class="form-group">
            <label>Diagnosis Notes</label>
            <textarea name="diagnosis" class="form-control" rows="3" required></textarea>
        </div>
        
        <hr style="border:0; border-top:1px solid #e5e7eb; margin:2rem 0;"/>
        
        <div style="display:flex;justify-content:space-between;align-items:center;margin-bottom:1rem;">
            <h3 style="margin:0;">Prescribe Medication</h3>
            <button type="button" id="addMedicineBtn" class="btn" style="background:#10B981;color:white;">+ Add Medicine</button>
        </div>
        
        <div id="medicineItemsContainer">
            <!-- Medicine JS generation -->
        </div>

        <div style="margin-top: 2rem;">
            <button type="submit" class="btn btn-primary" style="font-size: 1.1rem; padding: 0.8rem 2rem;">Complete Consultation & Auto-Bill</button>
        </div>
    </form>
</div>

<jsp:include page="../layout/footer.jsp" />
