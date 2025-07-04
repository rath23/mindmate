package com.maq.mindmate.exceptions;

public class SelfReportingException extends RuntimeException {
    public SelfReportingException(String message) {
        super(message);
    }
}