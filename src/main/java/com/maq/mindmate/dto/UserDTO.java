package com.maq.mindmate.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
    private UUID id;
    private String email;

    @Setter(AccessLevel.NONE)
    private String password;

    private String userName;

    private String name;

    private String nickName;

    private Boolean anonymousMode;

    private Boolean reminderEnabled;


    private Boolean notificationsEnabled;

    private LocalDateTime createdAt;

    private LocalDateTime lastLogin;

    private Boolean isDeleted;
}
