package com.maq.mindmate.services;

import com.maq.mindmate.dto.UserDTO;
import com.maq.mindmate.models.User;
import com.maq.mindmate.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    public UserDTO getCurrentUserInfo(UserDetails userDetails){
        String username = userDetails.getUsername();

        User currentUser = userRepo.findByUserName(username); // use the service method here

        UserDTO response = UserDTO.builder()
                .email(currentUser.getEmail())
                .userName(currentUser.getUserName())
                .name(currentUser.getName())
                .nickName(currentUser.getNickName())
                .anonymousMode(currentUser.getAnonymousMode())
                .reminderEnabled(currentUser.getReminderEnabled())
                .createdAt(currentUser.getCreatedAt())
                .lastLogin(currentUser.getLastLogin())
                .isDeleted(currentUser.getIsDeleted())
                .build();

        return response;
    }
}
