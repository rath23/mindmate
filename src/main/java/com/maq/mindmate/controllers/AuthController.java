package com.maq.mindmate.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.maq.mindmate.dto.LogInDTO;
import com.maq.mindmate.dto.UserDTO;
import com.maq.mindmate.services.JWTService;
import com.maq.mindmate.services.AuthService;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("auth/user/")
public class AuthController {

    @Autowired
    private AuthService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTService jwtService;

    @PostMapping("register")
    public UserDTO register(@Valid @RequestBody UserDTO user) {
        userService.register(user);
        return user;
    }

//    @PostMapping("login")
//    public ResponseEntity<?> login(@Valid @RequestBody LogInDTO user) {
//
//
//        Authentication authentication = authenticationManager
//                .authenticate(new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword()));
//        if (authentication.isAuthenticated()) {
//            userService.updateLastLogin(user.getUserName());
//
//            Map<String, String> response = new HashMap<>();
//            response.put("token", jwtService.generateToken(user.getUserName()));
//            return ResponseEntity.ok(response);
//        } else {
//            return ResponseEntity.badRequest().body("Login failed");
//        }
//    }

    @PostMapping("login")
    public ResponseEntity<?> login(@Valid @RequestBody LogInDTO user) {
        try {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(
                            user.getUserName(),
                            user.getPassword()
                    ));

            if (authentication.isAuthenticated()) {
                userService.updateLastLogin(user.getUserName());
                Map<String, String> response = new HashMap<>();
                response.put("token", jwtService.generateToken(user.getUserName()));
                return ResponseEntity.ok(response);
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Collections.singletonMap("error", "Invalid credentials"));

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Collections.singletonMap("error", "Invalid username or password"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "Server error: " + e.getMessage()));
        }
    }
    

    @PostMapping("refresh")
    public ResponseEntity<?> refreshToken(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body("Missing or invalid Authorization header");
        }

        String oldToken = authHeader.substring(7);

        if (!jwtService.isTokenExpired(oldToken)) {
            return ResponseEntity.status(401).body("Invalid or expired token");
        }

        String username = jwtService.extractUsername(oldToken);
        String newToken = jwtService.generateToken(username);

        Map<String, String> response = new HashMap<>();
        response.put("token", newToken);
        return ResponseEntity.ok(response);
    }

}

