package com.smartclinic.util;

import com.smartclinic.model.User;
import com.smartclinic.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private UserService userService;

    @Autowired
    private com.smartclinic.service.DoctorService doctorService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @SuppressWarnings("null")
    public void onApplicationEvent(ContextRefreshedEvent event) {
        // Ensure seeder only runs for the root context
        if (event.getApplicationContext().getParent() == null) {
            seedData();
        }
    }

    private void seedData() {
        if (userService.findByEmail("admin@smartclinic.com") == null) {
            User admin = new User();
            admin.setName("System Admin");
            admin.setEmail("admin@smartclinic.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole(User.Role.ADMIN);
            userService.save(admin);
            System.out.println("Default admin user seeded: admin@smartclinic.com / admin123");
        }

        if (userService.findByEmail("doctor@smartclinic.com") == null) {
            User docUser = new User();
            docUser.setName("Dr. John Smith");
            docUser.setEmail("doctor@smartclinic.com");
            docUser.setPassword(passwordEncoder.encode("doctor123"));
            docUser.setRole(User.Role.DOCTOR);
            userService.save(docUser);

            com.smartclinic.model.Doctor doctor = new com.smartclinic.model.Doctor();
            doctor.setUser(docUser);
            doctor.setSpecialization("General Physician");
            doctor.setAvailableDays("MONDAY,TUESDAY,WEDNESDAY,THURSDAY,FRIDAY");
            doctor.setSlotDurationMins(30);
            doctorService.save(doctor);
            
            System.out.println("Default doctor user and entity seeded: doctor@smartclinic.com / doctor123");
        }
    }
}
