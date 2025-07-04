package com.maq.mindmate.exceptions;

public class DuplicateMoodEntryException extends RuntimeException {
    public DuplicateMoodEntryException() {
        super("Mood entry for today already exists.");
    }
}