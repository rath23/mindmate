package com.maq.mindmate.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LogInDTO {

    @NotBlank(message = "Username is required")
    private String userName;

    @NotBlank(message = "Password is required")
    private String password;
}
