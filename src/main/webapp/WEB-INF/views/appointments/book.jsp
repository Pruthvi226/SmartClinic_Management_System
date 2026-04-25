<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../layout/header.jsp" />

<div class="card" style="max-width: 600px; margin: 0 auto;">
    <h2>Book Appointment</h2>
    
    <form action="<c:url value='/appointments/book'/>" method="POST">
        <div class="form-group">
            <label>Patient</label>
            <c:choose>
                <c:when test="${not empty patient}">
                    <input type="text" class="form-control" value="${patient.name}" readonly/>
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
        
        <div class="form-group" style="display:flex; gap:10px;">
            <div style="flex:1">
                <label>Date</label>
                <input type="date" id="dateSelect" class="form-control" required />
            </div>
            <div style="flex:1">
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
            <select name="slotTime" id="slotTimeSelect" class="form-control" required>
                <option value="">Select Date/Doctor first</option>
            </select>
        </div>
        
        <button type="submit" class="btn btn-primary">Confirm Booking</button>
    </form>
</div>

<jsp:include page="../layout/footer.jsp" />
