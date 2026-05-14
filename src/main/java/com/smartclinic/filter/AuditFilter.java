package com.smartclinic.filter;

import com.smartclinic.model.AuditLog;
import com.smartclinic.model.User;
import com.smartclinic.service.AuditLogService;
import com.smartclinic.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@WebFilter(urlPatterns = "/*")
public class AuditFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(AuditFilter.class);

    private AuditLogService auditLogService;
    private UserService userService;

    @Override
    @SuppressWarnings("null")
    public void init(FilterConfig filterConfig) throws ServletException {
        ApplicationContext ctx = WebApplicationContextUtils
                .getRequiredWebApplicationContext(filterConfig.getServletContext());
        this.auditLogService = ctx.getBean(AuditLogService.class);
        this.userService = ctx.getBean(UserService.class);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String uri = httpRequest.getRequestURI();
        
        // Skip static resources and login
        if (!uri.contains("/resources/") && !uri.equals("/login")) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            User user = null;
            if (auth != null && auth.isAuthenticated() && !auth.getPrincipal().equals("anonymousUser")) {
                org.springframework.security.core.userdetails.User secUser = (org.springframework.security.core.userdetails.User) auth.getPrincipal();
                user = userService.findByEmail(secUser.getUsername());
            }

            AuditLog log = new AuditLog();
            log.setAction("ACCESS " + uri);
            log.setIpAddress(request.getRemoteAddr());
            log.setUser(user); // Can be null for unauthenticated
            
            // Assuming business logic is fine with saving log here.
            try {
                auditLogService.save(log);
            } catch (Exception e) {
                logger.warn("Audit log write failed for {}", uri, e);
            }
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }
}
