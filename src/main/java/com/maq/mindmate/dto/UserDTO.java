package com.maq.mindmate.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {

    private UUID id;

    @NotBlank(message = "Email is required")
    @Email(message = "Email format is invalid")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    @Setter(AccessLevel.NONE)
    private String password;

    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 20, message = "Username must be between 3 to 20 characters")
    private String userName;

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Nickname is required")
    private String nickName;

//    @NotNull(message = "Anonymous mode must be specified")
    private Boolean anonymousMode;

//    @NotNull(message = "Reminder setting must be specified")
    private Boolean reminderEnabled;

//    @NotNull(message = "Notification setting must be specified")
    private Boolean notificationsEnabled;

    private LocalDateTime createdAt;

    private LocalDateTime lastLogin;

    private Boolean isDeleted;
}
