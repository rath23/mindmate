package com.maq.mindmate.exceptions;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<Map<String, Object>> buildResponse(HttpStatus status, String error, String message) {
        Map<String, Object> body = new HashMap<>();
        body.put("status", status.value());
        body.put("error", error);
        body.put("message", message);
        body.put("timestamp", LocalDateTime.now());
        return new ResponseEntity<>(body, status);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<?> handleUserNotFoundException(UserNotFoundException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.NOT_FOUND.value());
        response.put("error", "User Not Found");
        response.put("message", ex.getMessage());

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<?> handleUserAlreadyExists(UserAlreadyExistsException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.CONFLICT.value());
        response.put("error", "Conflict");
        response.put("message", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(InvalidUserDataException.class)
    public ResponseEntity<?> handleInvalidUserData(InvalidUserDataException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("error", "Bad Request");
        response.put("message", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserUpdateFailedException.class)
    public ResponseEntity<?> handleUserUpdateFailed(UserUpdateFailedException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("error", "Bad Request");
        response.put("message", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UnauthorizedAccessException.class)
    public ResponseEntity<?> handleUnauthorizedAccess(UnauthorizedAccessException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("error", "Bad Request");
        response.put("message", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(BadgeNotFoundException.class)
    public ResponseEntity<?> handleBadgeNotFound(BadgeNotFoundException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("error", "Bad Request");
        response.put("message", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<?> handleTaskNotFound(TaskNotFoundException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("error", "Bad Request");
        response.put("message", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AISuggestionException.class)
    public ResponseEntity<?> handleAISuggestionError(AISuggestionException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("error", "Bad Request");
        response.put("message", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_GATEWAY);
    }

    @ExceptionHandler(JournalEntryNotFoundException.class)
    public ResponseEntity<?> handleJournalNotFound(JournalEntryNotFoundException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("error", "Bad Request");
        response.put("message", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadWordsFileLoadException.class)
    public ResponseEntity<?> handleJournalNotFound(BadgeNotFoundException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("error", "Bad Request");
        response.put("message", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EmptyBadWordListException.class)
    public ResponseEntity<?> handleEmptyBadWordList(EmptyBadWordListException ex) {
        return buildResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Empty Bad Word List",
                ex.getMessage()
        );
    }

    @ExceptionHandler(DuplicateMoodEntryException.class)
    public ResponseEntity<?> handleDuplicateMood(DuplicateMoodEntryException ex) {
        return buildResponse(HttpStatus.CONFLICT, "Duplicate Mood Entry", ex.getMessage());
    }

    // Handle self-reporting
    @ExceptionHandler(SelfReportingException.class)
    public ResponseEntity<?> handleSelfReporting(SelfReportingException ex) {
        return buildResponse( HttpStatus.BAD_REQUEST,"Self Reporting error",ex.getMessage());
    }

    // Handle violation level exceeded
    @ExceptionHandler(BlockDurationExceededException.class)
    public ResponseEntity<?> handleBlockDurationExceeded(BlockDurationExceededException ex) {
        return buildResponse( HttpStatus.BAD_REQUEST,"Block Duration Exceed error.",ex.getMessage());
    }

    @ExceptionHandler(GeminiAPIException.class)
    public ResponseEntity<?> handleGeminiAPI(GeminiAPIException ex) {
        return buildResponse(HttpStatus.BAD_GATEWAY,"Gemini Api error", ex.getMessage());
    }

    @ExceptionHandler(JsonParsingException.class)
    public ResponseEntity<?> handleJsonParsing(JsonParsingException ex) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Json parsing error",ex.getMessage());
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<Object> handleDatabaseError(DataAccessException ex) {
        return (ResponseEntity<Object>) (ResponseEntity<?>) buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Database access error.", ex.getMessage());

    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<Object> handleNullPointer(NullPointerException ex) {
        return (ResponseEntity<Object>) (ResponseEntity<?>) buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected null value encountered.", ex.getMessage());
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.put("error", "Internal Server Error");
        response.put("message", ex.getMessage());

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
