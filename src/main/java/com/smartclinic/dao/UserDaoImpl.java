package com.smartclinic.dao;
import com.smartclinic.model.User;
import org.springframework.stereotype.Repository;
import org.hibernate.query.Query;

@Repository
public class UserDaoImpl extends GenericDaoImpl<User, Long> implements UserDao {
    @Override
    public User findByEmail(String email) {
        Query<User> query = getSession().createQuery("from User u where u.email = :email", User.class);
        query.setParameter("email", email);
        return query.uniqueResult();
    }
}
