package com.maq.mindmate.exceptions;

public class EmptyBadWordListException extends RuntimeException {
    public EmptyBadWordListException() {
        super("Bad word list is empty. Please ensure 'badwords.txt' is correctly loaded.");
    }
}
