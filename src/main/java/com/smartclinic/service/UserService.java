package com.smartclinic.service;

import com.smartclinic.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import java.util.List;

public interface UserService extends UserDetailsService {
    void save(User user);
    User findById(Long id);
    User findByEmail(String email);
    List<User> findAll();
    void toggleActive(Long id);
}
