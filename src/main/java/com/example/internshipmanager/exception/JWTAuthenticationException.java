package com.example.internshipmanager.exception;

public class JWTAuthenticationException extends RuntimeException {
    public JWTAuthenticationException(String message) {
        super(message);
    }
}
