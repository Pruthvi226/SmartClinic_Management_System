package com.smartclinic.config;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

public class WebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[] { AppConfig.class, SecurityConfig.class };
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[] { WebConfig.class };
    }

    @Override
    @SuppressWarnings("null")
    protected String[] getServletMappings() {
        return new String[] { "/" };
    }

    @Override
    @SuppressWarnings("null")
    public void onStartup(ServletContext servletContext) throws ServletException {
        super.onStartup(servletContext);
        // Use environment variable if available, otherwise default to 'prod'
        String activeProfile = System.getenv("SPRING_PROFILES_ACTIVE");
        if (activeProfile == null || activeProfile.isEmpty()) {
            activeProfile = "prod";
        }
        servletContext.setInitParameter("spring.profiles.active", activeProfile);
    }
}
