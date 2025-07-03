package com.maq.mindmate.exceptions;

public class UserUpdateFailedException extends RuntimeException {
    public UserUpdateFailedException(String message) {
        super(message);
    }
}