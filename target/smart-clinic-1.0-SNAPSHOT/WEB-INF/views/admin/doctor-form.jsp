<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<jsp:include page="../layout/header.jsp" />

<div class="card" style="max-width: 600px; margin: 0 auto;">
    <h2>Register New Doctor</h2>
    
    <form action="<c:url value='/admin/doctors/add'/>" method="post">
        <div class="form-group">
            <label>Full Name</label>
            <input type="text" name="name" required placeholder="Dr. Name" />
        </div>
        
        <div class="form-group">
            <label>Email Address</label>
            <input type="email" name="email" required placeholder="doctor@smartclinic.com" />
        </div>

        <div class="form-group">
            <label>Initial Password</label>
            <input type="password" name="password" required placeholder="Min 6 characters" />
        </div>

        <div class="form-group">
            <label>Specialization</label>
            <input type="text" name="specialization" required placeholder="e.g. Cardiologist" />
        </div>

        <div class="form-group">
            <label>Available Days (Comma separated)</label>
            <input type="text" name="availableDays" required value="MONDAY,TUESDAY,WEDNESDAY,THURSDAY,FRIDAY" />
        </div>

        <div class="form-group">
            <label>Slot Duration (Minutes)</label>
            <select name="slotDurationMins">
                <option value="15">15 mins</option>
                <option value="30" selected>30 mins</option>
                <option value="45">45 mins</option>
                <option value="60">60 mins</option>
            </select>
        </div>

        <div style="margin-top: 2rem; display: flex; gap: 1rem;">
            <button type="submit" class="btn btn-primary">Register Doctor</button>
            <a href="<c:url value='/admin/doctors'/>" class="btn btn-secondary">Cancel</a>
        </div>
    </form>
</div>

<jsp:include page="../layout/footer.jsp" />
