package com.mritunjay.cineticket.exception;

import org.springframework.http.HttpStatus;

public class TheatreConflictException extends CustomException {
    public TheatreConflictException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }
}