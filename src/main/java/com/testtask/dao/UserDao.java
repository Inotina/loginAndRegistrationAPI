package com.testtask.dao;

import com.testtask.entity.User;
import com.testtask.exception.NoSuchUserException;
import org.springframework.stereotype.Repository;

public interface UserDao {

    public User getByName(String name) throws NoSuchUserException;

    public User getByEmail(String email) throws NoSuchUserException;

    public User getByNameAndPassword(String name, String password) throws NoSuchUserException;

    public void add(User user);
}
