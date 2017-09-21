package com.testtask.service;

import com.testtask.dao.UserDao;
import com.testtask.entity.User;
import com.testtask.exception.NoSuchUserException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    private String errorMessage = "";
    private static final Logger log = Logger.getLogger(UserService.class);

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Transactional
    public boolean login(User user){
        if (user.getName() == null || user.getPassword() == null){
            return false;
        }
        User curUser = null;
        try {
            curUser = userDao.getByNameAndPassword(user.getName(), user.getPassword());
            return true;
        } catch (NoSuchUserException e) {
            log.debug("attempt to login user: " + user.getName(), e);
            return false;
        }
    }
    @Transactional
    public boolean checkUser(User user){
        return checkName(user) & checkEmail(user) & checkPassword(user) & checkRights(user);
    }
    @Transactional
    public boolean checkName(User user){
        if (user.getName() == null){
            errorMessage += " user login is null";
            return false;
        }
        if (user.getName().equals("")){
            errorMessage += " login field is empty";
            return false;
        }
        User curUser = null;
        try {
            curUser = userDao.getByName(user.getName());
            errorMessage += " login is alredy in use";
            return false;
        } catch (NoSuchUserException e) {
            log.debug("Login is not in use", e);
            return true;
        }
    }
    @Transactional
    public boolean checkEmail(User user){
        if (user.getEmail() == null){
            errorMessage += " user email is null";
            return false;
        }
        if (user.getEmail().length() < 4 || !user.getEmail().contains("@")){
            errorMessage += " email is not valid or contains less than 4 characters";
            return false;
        }
        User curUser = null;
        try {
            curUser = userDao.getByEmail(user.getEmail());
            errorMessage += " email is alredy in use";
            return false;
        } catch (NoSuchUserException e) {
            log.debug("Email is not in use", e);
            return true;
        }
    }

    public boolean checkPassword(User user){
        if (user.getPassword() == null){
            errorMessage += " user password is null";
            return false;
        }
        if (user.getPassword().length() < 8){
            errorMessage += " password is less than 8 characters";
            return false;
        }
        return true;
    }

    public boolean checkRights(User user){
        user.setIsAdmin("N");
        return true;
    }
    @Transactional
    public void addUser(User user){
        userDao.add(user);
    }
}
