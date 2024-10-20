package com.cpt.payments.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.cpt.payments.pojo.ErrorResponse;

@ControllerAdvice
public class ProcessingExceptionHandler {

	 // Handle ValidationException
    @ExceptionHandler(ProcessingException.class)
    public ResponseEntity<ErrorResponse> handleProcessingException(
    		ProcessingException ex) {
        // Create an error response object
        ErrorResponse errorResponse = new ErrorResponse(
        		ex.getErrorCode(), ex.getErrorMessage());

        System.out.println("ProcessingExceptionHandler handled | errorResponse:" + errorResponse);
        
        // Return the error response with the corresponding HTTP status
        return new ResponseEntity<>(errorResponse, ex.getHttpStatus());
    }
    
}
