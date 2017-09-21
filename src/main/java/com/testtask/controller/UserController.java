package com.testtask.controller;

import com.testtask.entity.User;
import com.testtask.model.Response;
import com.testtask.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public Response login(@RequestParam("login") String login, @RequestParam("password") String password){
        User user = new User(login, password);
        if( userService.login(user)){
            return new Response(true, "Loged in successfully!");
        }else{
            return new Response(false, "Invalid login or password");
        }
    }

    @RequestMapping(value = "register", method = RequestMethod.GET)
    public Response register(@RequestParam("login") String login, @RequestParam("password") String password, @RequestParam("email") String email){
        User user = new User(login, password, email);
        if (userService.checkUser(user)){
            userService.addUser(user);
            return new Response(true, "Registered successfully!");
        }else{
            String message = userService.getErrorMessage();
            userService.setErrorMessage("");
            return new Response(false, message);
        }
    }
}
