package com.maq.mindmate.controllers;

import com.maq.mindmate.dto.UpdateUserDto;
import com.maq.mindmate.dto.UserDTO;
import com.maq.mindmate.models.User;
import com.maq.mindmate.services.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private XPTrackingService xpTrackingService;

    @Autowired
    private SelfCareAIService selfCareAIService;

    @Autowired
    private  final GeminiTaskService geminiTaskService;

    @Autowired
    private HomeService homeService;

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser() {
        UserDetails currentUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
         UserDTO response = userService.getCurrentUserInfo(currentUser);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/ai-suggest")
    public ResponseEntity<?> getAISuggestions(Authentication authentication) {
        User user = userService.getCurrentUserDetailWithAuth(authentication);
        List<Map<String, String>> suggestions;
        return selfCareAIService.getAISuggestions(user);
    }

     @GetMapping("/progress")
    public  ResponseEntity<?> UserProgress (Authentication authentication){
        return ResponseEntity.ok().body(xpTrackingService.getUserProgress(authentication));
     }

     @PostMapping("/task-completed/{taskId}")
    public ResponseEntity<?> userTaskCompleted(@PathVariable Long taskId , Authentication authentication){
       User user = userService.getCurrentUserDetailWithAuth(authentication);
       geminiTaskService.markTaskComplete(taskId,user);
        return ResponseEntity.ok().body(Map.of("message", "Done Updating"));

     }

     @GetMapping("/home")
    public ResponseEntity<?> homeScreenDataFetch(Authentication authentication){
         User user = userService.getCurrentUserDetailWithAuth(authentication);
         return homeService.getHomeData(user);
     }

    @PutMapping("/update-profile")
    public ResponseEntity<?> updateProfile(@Valid @RequestBody UpdateUserDto dto, @AuthenticationPrincipal UserDetails userDetails) {
        return userService.updateUserInfo(dto, userDetails);
    }

}

