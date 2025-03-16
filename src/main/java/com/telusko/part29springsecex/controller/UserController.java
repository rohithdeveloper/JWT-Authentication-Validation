package com.telusko.part29springsecex.controller;

import com.telusko.part29springsecex.model.LoginResponse;
import com.telusko.part29springsecex.model.Users;
import com.telusko.part29springsecex.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserService service;


    @PostMapping("/register")
    public Users register(@RequestBody Users user) {
        return service.register(user);

    }

    @GetMapping("/users")
    public List<Users> getAll() {
        return service.allUsers();
    }


    @PostMapping("/login")
    public LoginResponse login(@RequestBody Users user, HttpSession session) {
        return service.verify(user, session);
    }


}
