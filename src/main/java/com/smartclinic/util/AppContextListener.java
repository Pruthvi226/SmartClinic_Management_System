package com.smartclinic.util;

import com.smartclinic.model.User;
import com.smartclinic.service.UserService;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class AppContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ApplicationContext ctx = WebApplicationContextUtils
                .getRequiredWebApplicationContext(sce.getServletContext());
        UserService userService = ctx.getBean(UserService.class);
        PasswordEncoder passwordEncoder = ctx.getBean(PasswordEncoder.class);

        if (userService.findByEmail("admin@smartclinic.com") == null) {
            User admin = new User();
            admin.setName("System Admin");
            admin.setEmail("admin@smartclinic.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole(User.Role.ADMIN);
            userService.save(admin);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }
}
