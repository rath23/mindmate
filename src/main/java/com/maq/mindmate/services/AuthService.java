package com.maq.mindmate.services;

import java.time.LocalDateTime;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.maq.mindmate.dto.UserDTO;
import com.maq.mindmate.models.User;
import com.maq.mindmate.repository.UserRepo;

@Service
public class AuthService {

    @Autowired
    private UserRepo userRepo;

    private final BCryptPasswordEncoder passwordEncoder;

    public AuthService(BCryptPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    public void register(UserDTO user) {
        User existingUser = userRepo.findByUserName(user.getUserName());
        if (existingUser != null) {
            throw new RuntimeException("User already exists");
        }
        User newUser = new User();

        // BeanUtils.copyProperties(user, newUser);
        newUser.setUserName(user.getUserName());
        newUser.setEmail(user.getEmail());
        newUser.setAnonymousMode(false);   
        newUser.setIsDeleted(false);
        newUser.setCreatedAt(LocalDateTime.now());
        newUser.setPassword(encodePassword(user.getPassword()));
        newUser.setName(user.getName());
        newUser.setNickName(user.getNickName());
        newUser.setReminderEnabled(false);
        userRepo.save(newUser);
    }

    public void updateLastLogin(String userName) {
        User user = userRepo.findByUserName(userName);
        if (user != null) {
            user.setLastLogin(LocalDateTime.now());
            userRepo.save(user);
        
        } else {
            throw new RuntimeException("User not found");
        }
    }

}