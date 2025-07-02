package com.maq.mindmate.dto;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;

@Data
@AllArgsConstructor
public class BadgeDTO{
    @Setter(AccessLevel.NONE)
    private Long id;


    private String name;

    private String description;


    private String colorStart;

    private String colorEnd;

    private boolean unlocked = false;
}
