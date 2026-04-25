package com.smartclinic.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {
    
    @GetMapping("/login")
    public String login() {
        return "login";
    }
    
    @GetMapping("/dashboard")
    public String dashboard() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN"))) {
            return "redirect:/admin/dashboard";
        } else if (auth.getAuthorities().contains(new SimpleGrantedAuthority("DOCTOR"))) {
            return "redirect:/doctor/schedule";
        } else if (auth.getAuthorities().contains(new SimpleGrantedAuthority("RECEPTIONIST"))) {
            return "redirect:/appointments/queue";
        } else if (auth.getAuthorities().contains(new SimpleGrantedAuthority("PHARMACIST"))) {
            return "redirect:/pharmacy/queue";
        }
        return "dashboard";
    }
    
    @GetMapping("/access-denied")
    public String accessDenied() {
        return "error/403";
    }
}
