package com.springapplication.blogappproject.exception;

import com.springapplication.blogappproject.payload.ErrorDetails;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    // handle specific exceptions
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorDetails> handleResourceNotFoundException(ResourceNotFoundException exception,
                                                                        WebRequest webRequest){
        ErrorDetails errorDetails = new ErrorDetails(new Date(), exception.getMessage(),
                webRequest.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BlogAPIException.class)
    public ResponseEntity<ErrorDetails> handleBlogAPIException(BlogAPIException exception,
                                                               WebRequest webRequest){
        ErrorDetails errorDetails = new ErrorDetails(new Date(), exception.getMessage(),
                webRequest.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    // global exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetails> handleGlobalException(Exception exception,
                                                              WebRequest webRequest){
        ErrorDetails errorDetails = new ErrorDetails(new Date(), exception.getMessage(),
                webRequest.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }

/**
 * Handles validation errors for method arguments.
 *
 * @param ex      the MethodArgumentNotValidException
 * @param headers the headers to be written to the response
 * @param status  the selected response status
 * @param request the current request
 * @return ResponseEntity containing validation errors
 */

protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                            HttpHeaders headers,
                                                            HttpStatusCode status,
                                                            WebRequest request) {
    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult().getAllErrors().forEach((error) -> {
        String fieldName = "unknown";
        String message = "Validation failed";
        
        if (error instanceof FieldError fieldError) {
            fieldName = fieldError.getField();
            message = fieldError.getDefaultMessage();
        } else if (error != null) {
            fieldName = error.getObjectName();
            message = error.getDefaultMessage();
        }
        
        errors.put(fieldName, message != null ? message : "Validation failed");
    });

    ErrorDetails errorDetails = new ErrorDetails(
        new Date(),
        "Validation failed",
        errors.toString()
    );
    
    return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
}
}