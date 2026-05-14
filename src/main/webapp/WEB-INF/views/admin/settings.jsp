<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../layout/header.jsp" />

<div class="card" style="max-width:720px; margin:0 auto;">
    <h2 style="margin-top:0;">System Settings</h2>
    <c:if test="${param.saved != null}">
        <div style="color:#16A34A; background:#F0FDF4; border:1px solid #DCFCE7; padding:0.8rem; border-radius:8px; margin-bottom:1rem;">Settings saved.</div>
    </c:if>
    <form action="<c:url value='/admin/settings'/>" method="post">
        <div class="form-group"><label>Hospital Name</label><input name="hospitalName" class="form-control" value="${settings['hospital.name']}" /></div>
        <div style="display:grid; grid-template-columns: repeat(3, 1fr); gap:1rem;">
            <div class="form-group"><label>Normal Fee</label><input type="number" step="0.01" name="normalFee" class="form-control" value="${settings['consultation.fee.normal']}" /></div>
            <div class="form-group"><label>Emergency Fee</label><input type="number" step="0.01" name="emergencyFee" class="form-control" value="${settings['consultation.fee.emergency']}" /></div>
            <div class="form-group"><label>Senior Fee</label><input type="number" step="0.01" name="seniorFee" class="form-control" value="${settings['consultation.fee.senior']}" /></div>
        </div>
        <div class="form-group"><label>Tax Rate</label><input type="number" step="0.01" name="taxRate" class="form-control" value="${settings['tax.rate']}" /></div>
        <button class="btn btn-primary">Save Settings</button>
    </form>
</div>

<jsp:include page="../layout/footer.jsp" />
