package com.maq.mindmate.exceptions;

public class TaskNotFoundException extends RuntimeException {
    public TaskNotFoundException(Long taskId) {
        super("Task with ID " + taskId + " not found for the current user.");
    }
}