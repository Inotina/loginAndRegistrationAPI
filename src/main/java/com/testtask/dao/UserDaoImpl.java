package com.testtask.dao;

import com.testtask.entity.User;
import com.testtask.exception.NoSuchUserException;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class UserDaoImpl implements UserDao{

    private SessionFactory sessionFactory;
    private static final Logger log = Logger.getLogger(UserDaoImpl.class);

    public UserDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public User getByName(String name) throws NoSuchUserException {
        CriteriaBuilder builder = getSession().getCriteriaBuilder();
        CriteriaQuery<User> cq =  builder.createQuery(User.class);
        Root<User> root = cq.from(User.class);
        cq.select(root);
        cq.where(builder.equal(root.get("name"), name));
        List<User> list = getSession().createQuery(cq).getResultList();
        if (list.size() == 0){
            log.debug(name + " - no user with this name in db.");
            throw new NoSuchUserException("No user in db with name: " + name);
        }
        log.debug("user with name - " + name + " was selected from db.");
        return list.get(0);
    }

    public User getByEmail(String email) throws NoSuchUserException {
        CriteriaBuilder builder = getSession().getCriteriaBuilder();
        CriteriaQuery<User> cq =  builder.createQuery(User.class);
        Root<User> root = cq.from(User.class);
        cq.select(root);
        cq.where(builder.equal(root.get("email"), email));
        List<User> list = getSession().createQuery(cq).getResultList();
        if (list.size() == 0){
            log.debug(email + " - no user with this email in db.");
            throw new NoSuchUserException("No user in db with this email: " + email);
        }
        log.debug("user with email - " + email + " was selected from db.");
        return list.get(0);
    }

    public User getByNameAndPassword(String name, String password) throws NoSuchUserException {
        CriteriaBuilder builder = getSession().getCriteriaBuilder();
        CriteriaQuery<User> cq =  builder.createQuery(User.class);
        Root<User> root = cq.from(User.class);
        cq.select(root);
        cq.where(builder.equal(root.get("name"), name), builder.equal(root.get("password"), password));
        List<User> list = getSession().createQuery(cq).getResultList();
        if (list.size() == 0){
            log.info("login with login - " + name + " and password "+ password + " failed.");
            throw new NoSuchUserException("login with login - " + name + " and password "+ password + " failed.");
        }
        log.info("user " + name + " successfully loged in.");
        return list.get(0);
    }

    public void add(User user) {
        getSession().save(user);
        log.info("user with name " + user.getName() + " was added.");
    }

    private Session getSession(){
        return sessionFactory.getCurrentSession();
    }
}
