package com.maq.mindmate.controllers;

import com.maq.mindmate.dto.UserDTO;
import com.maq.mindmate.services.SelfCareAIService;
import com.maq.mindmate.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private SelfCareAIService selfCareAIService;


    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser() {
        UserDetails currentUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
         UserDTO response = userService.getCurrentUserInfo(currentUser);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/ai-suggest")
    public ResponseEntity<?> getAISuggestions(Authentication authentication) {
        return selfCareAIService.getAISuggestions(authentication);
    }

}

