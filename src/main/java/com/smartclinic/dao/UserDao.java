package com.smartclinic.dao;
import com.smartclinic.model.User;
public interface UserDao extends GenericDao<User, Long> {
    User findByEmail(String email);
}
