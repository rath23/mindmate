package com.maq.mindmate.exceptions;

public class JournalEntryNotFoundException extends RuntimeException {
    public JournalEntryNotFoundException(String message) {
        super(message);
    }

}