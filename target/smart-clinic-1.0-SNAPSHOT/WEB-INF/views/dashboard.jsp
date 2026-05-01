<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<jsp:include page="layout/header.jsp" />

<div class="card" style="background: linear-gradient(135deg, var(--primary) 0%, var(--primary-hover) 100%); color: white; border: none;">
    <h1 style="margin:0; font-size: 2.5rem;">Welcome back, <sec:authentication property="principal.username" />!</h1>
    <p style="font-size: 1.1rem; opacity: 0.9; margin-top: 1rem;">The SmartClinic platform is active and monitoring all hospital modules.</p>
</div>

<div style="display:grid; grid-template-columns: repeat(auto-fit, minmax(300px, 1fr)); gap:1.5rem; margin-top:1.5rem;">
    <sec:authorize access="hasAuthority('RECEPTIONIST') or hasAuthority('ADMIN')">
        <div class="card" style="margin-bottom:0;">
            <div style="background:rgba(79, 70, 229, 0.1); width:40px; height:40px; border-radius:10px; display:flex; align-items:center; justify-content:center; color:var(--primary); margin-bottom:1rem;">
                <svg class="icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2"/><circle cx="9" cy="7" r="4"/><path d="M23 21v-2a4 4 0 0 0-3-3.87"/><path d="M16 3.13a4 4 0 0 1 0 7.75"/></svg>
            </div>
            <h3 style="margin:0">Patient Management</h3>
            <p style="color:var(--text-muted); font-size:0.875rem; margin:0.5rem 0 1.5rem;">Register new patients or search existing records.</p>
            <div style="display:flex; gap:0.5rem;">
                <a href="<c:url value='/patients/search'/>" class="btn btn-primary" style="font-size:0.8rem;">Patient Search</a>
                <a href="<c:url value='/patients/register'/>" class="btn btn-secondary" style="font-size:0.8rem;">New Patient</a>
            </div>
        </div>
        <div class="card" style="margin-bottom:0;">
            <div style="background:rgba(16, 185, 129, 0.1); width:40px; height:40px; border-radius:10px; display:flex; align-items:center; justify-content:center; color:var(--secondary); margin-bottom:1rem;">
                <svg class="icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"/><polyline points="14 2 14 8 20 8"/><line x1="16" y1="13" x2="8" y2="13"/><line x1="16" y1="17" x2="8" y2="17"/><polyline points="10 9 9 9 8 9"/></svg>
            </div>
            <h3 style="margin:0">Live Queue</h3>
            <p style="color:var(--text-muted); font-size:0.875rem; margin:0.5rem 0 1.5rem;">Monitor appointment status and patient flow.</p>
            <a href="<c:url value='/appointments/queue'/>" class="btn btn-primary" style="font-size:0.8rem;">View Queue</a>
        </div>
    </sec:authorize>

    <sec:authorize access="hasAuthority('DOCTOR')">
        <div class="card" style="margin-bottom:0;">
            <div style="background:rgba(245, 158, 11, 0.1); width:40px; height:40px; border-radius:10px; display:flex; align-items:center; justify-content:center; color:var(--senior); margin-bottom:1rem;">
                <svg class="icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><rect x="3" y="4" width="18" height="18" rx="2" ry="2"/><line x1="16" y1="2" x2="16" y2="6"/><line x1="8" y1="2" x2="8" y2="6"/><line x1="3" y1="10" x2="21" y2="10"/></svg>
            </div>
            <h3 style="margin:0">Consultations</h3>
            <p style="color:var(--text-muted); font-size:0.875rem; margin:0.5rem 0 1.5rem;">View your daily schedule and conduct patient consultations.</p>
            <a href="<c:url value='/doctor/schedule'/>" class="btn btn-primary" style="font-size:0.8rem;">My Schedule</a>
        </div>
    </sec:authorize>
</div>

<jsp:include page="layout/footer.jsp" />
