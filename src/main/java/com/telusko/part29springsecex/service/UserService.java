package com.telusko.part29springsecex.service;

import com.telusko.part29springsecex.model.LoginResponse;
import com.telusko.part29springsecex.model.Users;
import com.telusko.part29springsecex.repo.UserRepo;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private JWTService jwtService;

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private UserRepo repo;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public Users register(Users user) {
        user.setPassword(encoder.encode(user.getPassword()));
        repo.save(user);
        return user;
    }

    public List<Users> allUsers() {
        List<Users> users = new ArrayList<>();
        repo.findAll().forEach(users::add);
        return users;
    }

    public LoginResponse verify(Users user, HttpSession session) {
        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
        );

        if (authentication.isAuthenticated()) {
            session.setAttribute("user", user.getUsername()); // Store username in session

            // Generate JWT Token
            String token = jwtService.generateToken(user.getUsername());

            // Create and return a LoginResponse object
            LoginResponse loginResponse = new LoginResponse();
            loginResponse.setToken(token);
            loginResponse.setExpiresIn(jwtService.getExpirationTime());
            loginResponse.setMessage("Login Successful");
            return loginResponse;
        } else {
            throw new UsernameNotFoundException("Login failed! Please provide correct credentials.");
        }
    }
}

