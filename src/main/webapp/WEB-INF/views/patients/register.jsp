<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../layout/header.jsp" />

<div class="card" style="max-width: 600px; margin: 0 auto;">
    <h2>Register New Patient</h2>
    
    <form action="<c:url value='/patients/register'/>" method="POST">
        <div class="form-group">
            <label>Full Name</label>
            <input type="text" name="name" class="form-control" required/>
        </div>
        <div class="form-group" style="display:flex; gap:1rem;">
            <div style="flex:1">
                <label>Date of Birth</label>
                <input type="date" name="dob" class="form-control" required/>
            </div>
            <div style="flex:1">
                <label>Gender</label>
                <select name="gender" class="form-control" required>
                    <option value="Male">Male</option>
                    <option value="Female">Female</option>
                    <option value="Other">Other</option>
                </select>
            </div>
        </div>
        <div class="form-group">
            <label>Phone Number</label>
            <input type="text" name="phone" class="form-control" required/>
        </div>
        <div class="form-group">
            <label>Email</label>
            <input type="email" name="email" class="form-control"/>
        </div>
        <div class="form-group">
            <label>Blood Group</label>
            <input type="text" name="bloodGroup" class="form-control"/>
        </div>
        <div class="form-group">
            <label>Address</label>
            <textarea name="address" class="form-control" rows="3"></textarea>
        </div>
        <button type="submit" class="btn btn-primary">Register Patient</button>
    </form>
</div>

<jsp:include page="../layout/footer.jsp" />
