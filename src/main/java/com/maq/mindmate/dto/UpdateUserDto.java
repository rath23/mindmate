package com.maq.mindmate.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;


@Data
public class UpdateUserDto {
    @NotBlank(message = "Email is required")
    @Email(message = "Email format is invalid")
    private String email;


    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 20, message = "Username must be between 3 to 20 characters")
    private String userName;

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Nickname is required")
    private String nickName;


}
