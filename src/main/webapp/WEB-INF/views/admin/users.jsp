<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../layout/header.jsp" />

<div style="display:grid; grid-template-columns:360px 1fr; gap:1.5rem; align-items:start;">
    <div class="card">
        <h2 style="margin-top:0;">${editMode ? 'Edit User' : 'Create User'}</h2>
        <c:choose>
            <c:when test="${editMode}">
                <c:url var="userAction" value="/admin/users/${user.id}/edit" />
            </c:when>
            <c:otherwise>
                <c:url var="userAction" value="/admin/users" />
            </c:otherwise>
        </c:choose>
        <form action="${userAction}" method="post">
            <div class="form-group"><label>Name</label><input name="name" class="form-control" required value="${user.name}" /></div>
            <div class="form-group"><label>Email</label><input type="email" name="email" class="form-control" required value="${user.email}" /></div>
            <div class="form-group"><label>Password</label><input type="password" name="password" class="form-control" <c:if test="${!editMode}">required</c:if> placeholder="${editMode ? 'Leave blank to keep current' : ''}" /></div>
            <div class="form-group">
                <label>Role</label>
                <select name="role" class="form-control">
                    <option value="ADMIN" <c:if test="${user.role == 'ADMIN'}">selected</c:if>>Admin</option>
                    <option value="DOCTOR" <c:if test="${user.role == 'DOCTOR'}">selected</c:if>>Doctor</option>
                    <option value="RECEPTIONIST" <c:if test="${user.role == 'RECEPTIONIST'}">selected</c:if>>Receptionist</option>
                    <option value="PHARMACIST" <c:if test="${user.role == 'PHARMACIST'}">selected</c:if>>Pharmacist</option>
                </select>
            </div>
            <c:if test="${editMode}">
                <label style="display:flex; gap:0.5rem; align-items:center; margin-bottom:1rem;"><input type="checkbox" name="active" <c:if test="${user.active}">checked</c:if> /> Active</label>
            </c:if>
            <button class="btn btn-primary" style="width:100%;">Save User</button>
        </form>
    </div>

    <div class="card">
        <h2 style="margin-top:0;">User Management</h2>
        <table>
            <thead><tr><th>Name</th><th>Email</th><th>Role</th><th>Active</th><th>Actions</th></tr></thead>
            <tbody>
                <c:forEach var="u" items="${users}">
                    <tr>
                        <td>${u.name}</td>
                        <td>${u.email}</td>
                        <td>${u.role}</td>
                        <td><span class="badge ${u.active ? 'badge-normal' : 'badge-senior'}">${u.active ? 'YES' : 'NO'}</span></td>
                        <td style="display:flex; gap:0.4rem;">
                            <a href="<c:url value='/admin/users/${u.id}/edit'/>" class="btn btn-secondary" style="padding:0.35rem 0.7rem;">Edit</a>
                            <form action="<c:url value='/admin/users/${u.id}/toggle'/>" method="post" style="margin:0;">
                                <button class="btn btn-secondary" style="padding:0.35rem 0.7rem;">Toggle</button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
</div>

<jsp:include page="../layout/footer.jsp" />
