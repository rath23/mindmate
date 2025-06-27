package com.maq.mindmate.controllers;

import com.maq.mindmate.dto.UserDTO;
import com.maq.mindmate.models.User;
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

    @GetMapping
    public String test(){
        return  "Hello";
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser() {
        UserDetails currentUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
         UserDTO response = userService.getCurrentUserInfo(currentUser);

//
//        UserDTO response = UserDTO.builder()
//                .email(currentUser.getEmail())
//                .userName(currentUser.getUserName())
//                .name(currentUser.getName())
//                .nickName(currentUser.getNickName())
//                .anonymousMode(currentUser.getAnonymousMode())
//                .reminderEnabled(currentUser.getReminderEnabled())
//                .createdAt(currentUser.getCreatedAt())
//                .lastLogin(currentUser.getLastLogin())
//                .isDeleted(currentUser.getIsDeleted())
//                .build();

        return ResponseEntity.ok(response);
    }

}

