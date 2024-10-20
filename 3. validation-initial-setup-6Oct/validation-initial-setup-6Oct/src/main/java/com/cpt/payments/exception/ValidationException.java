package com.cpt.payments.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ValidationException extends RuntimeException {

	private static final long serialVersionUID = 5669001885888213754L;

	private final int errorCode;
    private final String errorMessage;
    private final HttpStatus httpStatus;

    // Constructor with all fields
    public ValidationException(int errorCode, String errorMessage, HttpStatus httpStatus) {
        super(errorMessage); // Setting the errorMessage as the exception message
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.httpStatus = httpStatus;
    }

   
}

