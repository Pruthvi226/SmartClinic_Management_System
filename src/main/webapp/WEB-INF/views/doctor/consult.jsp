<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../layout/header.jsp" />

<div class="card">
    <h2>Consultation: ${appointment.patient.name}</h2>
    <div style="margin-bottom: 2rem; background: #f9fafb; padding:1rem; border-radius:8px;">
        <strong>Dob:</strong> ${appointment.patient.dob} | 
        <strong>Blood:</strong> ${appointment.patient.bloodGroup} | 
        <strong>Allergies:</strong> <span id="patientAllergies" data-allergies="${appointment.patient.allergies}">${empty appointment.patient.allergies ? 'None recorded' : appointment.patient.allergies}</span>
    </div>
    <div id="allergyWarning" style="display:none; color:#991B1B; background:#FEF2F2; border:1px solid #FECACA; padding:1rem; border-radius:8px; margin-bottom:1rem;">
        Allergy warning: review the selected medicine before completing this prescription.
    </div>

    <form action="<c:url value='/doctor/consult/${appointment.id}'/>" method="POST">
        <div style="display:grid; grid-template-columns: 1fr 1fr 1fr; gap:1rem;">
            <div class="form-group">
                <label>Clinical Template</label>
                <select name="clinicalTemplate" class="form-control">
                    <option value="">Custom notes</option>
                    <c:forEach var="template" items="${clinicalTemplates}">
                        <option value="${template}" <c:if test="${prescription.clinicalTemplate == template}">selected</c:if>>${template}</option>
                    </c:forEach>
                </select>
            </div>
            <div class="form-group">
                <label>Diagnosis Tag</label>
                <select name="diagnosisTag" class="form-control">
                    <option value="">Select tag</option>
                    <c:forEach var="tag" items="${diagnosisTags}">
                        <option value="${tag}" <c:if test="${prescription.diagnosisTag == tag}">selected</c:if>>${tag}</option>
                    </c:forEach>
                </select>
            </div>
            <div class="form-group">
                <label>Prescription Favorite</label>
                <select name="favoriteName" class="form-control">
                    <option value="">No favorite</option>
                    <c:forEach var="favorite" items="${prescriptionFavorites}">
                        <option value="${favorite}" <c:if test="${prescription.favoriteName == favorite}">selected</c:if>>${favorite}</option>
                    </c:forEach>
                </select>
            </div>
        </div>

        <div style="display:grid; grid-template-columns: repeat(5, 1fr); gap:0.8rem;">
            <div class="form-group">
                <label>BP</label>
                <input type="text" name="bloodPressure" class="form-control" placeholder="120/80" value="${prescription.bloodPressure}" />
            </div>
            <div class="form-group">
                <label>Pulse</label>
                <input type="text" name="pulse" class="form-control" placeholder="78 bpm" value="${prescription.pulse}" />
            </div>
            <div class="form-group">
                <label>Temp</label>
                <input type="text" name="temperature" class="form-control" placeholder="98.6 F" value="${prescription.temperature}" />
            </div>
            <div class="form-group">
                <label>SpO2</label>
                <input type="text" name="spo2" class="form-control" placeholder="98%" value="${prescription.spo2}" />
            </div>
            <div class="form-group">
                <label>Weight</label>
                <input type="text" name="weightKg" class="form-control" placeholder="70 kg" value="${prescription.weightKg}" />
            </div>
        </div>

        <div class="form-group">
            <label>Diagnosis Notes</label>
            <textarea name="diagnosis" class="form-control" rows="4" required>${prescription.diagnosis}</textarea>
        </div>

        <div style="display:grid; grid-template-columns: 1fr 1fr 180px; gap:1rem;">
            <div class="form-group">
                <label>Lab Orders</label>
                <textarea name="labOrders" class="form-control" rows="3" placeholder="CBC, ECG, Lipid profile">${prescription.labOrders}</textarea>
            </div>
            <div class="form-group">
                <label>Risk Flags</label>
                <textarea name="riskFlags" class="form-control" rows="3" placeholder="High BP, allergy risk, cardiac risk">${prescription.riskFlags}</textarea>
            </div>
            <div class="form-group">
                <label>Follow-up Days</label>
                <input type="number" min="0" name="followUpDays" class="form-control" value="${prescription.followUpDays}" />
            </div>
        </div>
        
        <hr style="border:0; border-top:1px solid #e5e7eb; margin:2rem 0;"/>
        
        <div style="display:flex;justify-content:space-between;align-items:center;margin-bottom:1rem;">
            <h3 style="margin:0;">Prescribe Medication</h3>
            <button type="button" id="addMedicineBtn" class="btn" style="background:#10B981;color:white;">+ Add Medicine</button>
        </div>
        
        <div id="medicineItemsContainer">
            <c:forEach var="item" items="${prescription.items}" varStatus="loop">
                <div class="prescription-item" style="display:grid;grid-template-columns:1.4fr 0.8fr 0.7fr 0.9fr 1.4fr auto;gap:10px;margin-bottom:10px;align-items:center;">
                    <input type="text" name="items[${loop.index}].medicineName" placeholder="Medicine" class="form-control medicine-name-input" required value="${item.medicineName}"/>
                    <input type="text" name="items[${loop.index}].dosage" placeholder="Dosage" class="form-control" required value="${item.dosage}"/>
                    <input type="number" min="1" name="items[${loop.index}].quantity" value="${item.quantity}" placeholder="Qty" class="form-control" required/>
                    <input type="text" name="items[${loop.index}].duration" placeholder="Duration" class="form-control" required value="${item.duration}"/>
                    <input type="text" name="items[${loop.index}].instructions" placeholder="Instructions" class="form-control" value="${item.instructions}"/>
                    <button type="button" class="btn btn-remove" style="background:#EF4444;color:white;" onclick="$(this).parent().remove()">X</button>
                </div>
            </c:forEach>
        </div>

        <div style="margin-top: 2rem; display:flex; gap:1rem;">
            <button type="submit" name="action" value="DRAFT" formnovalidate class="btn btn-secondary" style="font-size: 1rem; padding: 0.8rem 1.5rem;">Save Draft</button>
            <button type="submit" name="action" value="COMPLETE" class="btn btn-primary" style="font-size: 1.1rem; padding: 0.8rem 2rem;">Complete Consultation & Auto-Bill</button>
        </div>
    </form>
</div>

<jsp:include page="../layout/footer.jsp" />
