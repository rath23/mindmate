package com.maq.mindmate.exceptions;

public class BadgeNotFoundException extends RuntimeException {
    public BadgeNotFoundException(String badgeName) {
        super("Badge not found: " + badgeName);
    }
}