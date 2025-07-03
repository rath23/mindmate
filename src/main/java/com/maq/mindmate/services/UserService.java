package com.maq.mindmate.services;

import com.maq.mindmate.dto.UserDTO;
import com.maq.mindmate.exceptions.InvalidUserDataException;
import com.maq.mindmate.exceptions.UserAlreadyExistsException;
import com.maq.mindmate.exceptions.UserNotFoundException;
import com.maq.mindmate.exceptions.UserUpdateFailedException;
import com.maq.mindmate.models.User;
import com.maq.mindmate.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
                .id(currentUser.getId())
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
    public User getUser(UserDetails userDetails) {
        String username = userDetails.getUsername();
        User currentUser = userRepo.findByUserName(username);
        if (currentUser == null) {
            throw new UserNotFoundException("User with username " + username + " not found");
        }
        return currentUser;
    }

    public User getCurrentUserDetailWithAuth( Authentication authentication){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = getUser(userDetails);
        return  user;
    }

    public ResponseEntity<?> updateUserInfo(UserDTO updatedInfo, UserDetails userDetails) {

        String username = userDetails.getUsername();
        User user = userRepo.findByUserName(username);
        if (!user.getUserName().equals(updatedInfo.getUserName()) &&
                userRepo.findByUserName(updatedInfo.getUserName()) != null) {
            throw new UserAlreadyExistsException("Username is already taken.");
        }
        if (updatedInfo.getEmail() != null && !updatedInfo.getEmail().contains("@")) {
            throw new InvalidUserDataException("Invalid email format.");
        }

        if (updatedInfo.getName() != null) user.setName(updatedInfo.getName());
        if (updatedInfo.getNickName() != null) user.setNickName(updatedInfo.getNickName());
        if (updatedInfo.getEmail() != null) user.setEmail(updatedInfo.getEmail());
        if (updatedInfo.getUserName() != null) user.setUserName(updatedInfo.getUserName());
        if (updatedInfo.getReminderEnabled() != null) user.setReminderEnabled(updatedInfo.getReminderEnabled());
        if (updatedInfo.getAnonymousMode() != null) user.setAnonymousMode(updatedInfo.getAnonymousMode());
        try {
            userRepo.save(user);
        } catch (Exception e) {
            throw new UserUpdateFailedException("User update failed due to internal error.");
        }


        return ResponseEntity.ok().body(UserDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .userName(user.getUserName())
                .name(user.getName())
                .nickName(user.getNickName())
                .anonymousMode(user.getAnonymousMode())
                .reminderEnabled(user.getReminderEnabled())
                .createdAt(user.getCreatedAt())
                .lastLogin(user.getLastLogin())
                .isDeleted(user.getIsDeleted())
                .build());
    }

}
