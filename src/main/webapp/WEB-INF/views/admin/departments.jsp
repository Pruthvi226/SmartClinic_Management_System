<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../layout/header.jsp" />

<div style="display:grid; grid-template-columns:360px 1fr; gap:1.5rem; align-items:start;">
    <div class="card">
        <h2 style="margin-top:0;">Department</h2>
        <form action="<c:url value='/admin/departments'/>" method="post">
            <div class="form-group"><label>Name</label><input name="name" class="form-control" required placeholder="Dermatology" /></div>
            <div class="form-group"><label>Description</label><textarea name="description" class="form-control" rows="3"></textarea></div>
            <label style="display:flex; gap:0.5rem; align-items:center; margin-bottom:1rem;"><input type="checkbox" name="active" checked /> Active</label>
            <button class="btn btn-primary" style="width:100%;">Save Department</button>
        </form>
    </div>

    <div class="card">
        <h2 style="margin-top:0;">Department/Specialization Management</h2>
        <table>
            <thead><tr><th>Name</th><th>Description</th><th>Status</th><th>Action</th></tr></thead>
            <tbody>
                <c:forEach var="d" items="${departments}">
                    <tr>
                        <td><strong>${d.name}</strong></td>
                        <td>${d.description}</td>
                        <td><span class="badge ${d.active ? 'badge-normal' : 'badge-senior'}">${d.active ? 'ACTIVE' : 'INACTIVE'}</span></td>
                        <td>
                            <form action="<c:url value='/admin/departments/${d.id}/toggle'/>" method="post" style="margin:0;">
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
