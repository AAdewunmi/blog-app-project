package com.springapplication.blogappproject.exception;

import com.springapplication.blogappproject.payload.ErrorDetails;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * GlobalExceptionHandler is a class annotated with @ControllerAdvice that provides centralized exception handling
 * across the entire application. It defines methods to handle various types of exceptions and ensures that
 * consistent and user-friendly error responses are sent in case of errors.
 */
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    
    /**
     * Handles exceptions of type {@code MethodArgumentNotValidException}.
     * This method is triggered when a method argument annotated with validation annotations fails validation.
     * It extracts validation error details, including the field name and default error message,
     * and creates an {@code ErrorDetails} object containing these details. The response entity
     * is returned with a {@code BAD_REQUEST} HTTP status.
     *
     * @param ex the {@code MethodArgumentNotValidException} that was thrown
     * @param headers the HTTP headers for the current request
     * @param status the HTTP status code of the response
     * @param request the current web request
     * @return a {@code ResponseEntity} containing the {@code ErrorDetails} object and a {@code BAD_REQUEST} HTTP status
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {
        
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        
        ErrorDetails errorDetails = new ErrorDetails(
            new Date(),
            "Validation Failed",
            errors.toString()
        );
        
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }


    /**
     * Handles exceptions of type {@code BlogAPIException}.
     * This method creates an {@code ErrorDetails} object containing the timestamp, exception message,
     * and additional details derived from the {@code WebRequest}, and returns it as a response entity
     * with a {@code BAD_REQUEST} HTTP status.
     *
     * @param exception the instance of {@code BlogAPIException} that was thrown
     * @param webRequest the current web request details, providing context and additional information
     * @return a {@code ResponseEntity} containing the {@code ErrorDetails} object and a {@code BAD_REQUEST} HTTP status
     */
    @ExceptionHandler(BlogAPIException.class)
    public ResponseEntity<ErrorDetails> handleBlogAPIException(BlogAPIException exception,
                                                               WebRequest webRequest){
        ErrorDetails errorDetails = new ErrorDetails(new Date(), exception.getMessage(),
                webRequest.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles global exceptions throughout the application.
     * This method is designed to catch general exceptions that are not specifically handled
     * by other exception handlers and provide a standardized response format.
     *
     * @param exception the exception that was thrown
     * @param webRequest the current web request during which the exception occurred
     * @return a ResponseEntity containing the error details and HTTP status INTERNAL_SERVER_ERROR
     */
    // global exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetails> handleGlobalException(Exception exception,
                                                              WebRequest webRequest){
        ErrorDetails errorDetails = new ErrorDetails(new Date(), exception.getMessage(),
                webRequest.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Handles exceptions of type AccessDeniedException.
     * This method constructs an ErrorDetails object containing the timestamp, message,
     * and additional details about the exception, and returns it wrapped in a ResponseEntity.
     *
     * @param exception the AccessDeniedException that was thrown
     * @param webRequest the current web request in which the exception occurred
     * @return a ResponseEntity containing the error details and a status of UNAUTHORIZED
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorDetails> handleAccessDeniedException(AccessDeniedException exception,
                                                                    WebRequest webRequest){
        ErrorDetails errorDetails = new ErrorDetails(new Date(), exception.getMessage(),
                webRequest.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.UNAUTHORIZED);
    }

    /**
     * Handles exceptions of type ResourceNotFoundException.
     * This method creates an ErrorDetails object containing information about the resource
     * that was not found and returns it with an HTTP NOT_FOUND status.
     *
     * @param exception the ResourceNotFoundException that was thrown
     * @param webRequest the current web request in which the exception occurred
     * @return a ResponseEntity containing the error details and NOT_FOUND status
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorDetails> handleResourceNotFoundException(ResourceNotFoundException exception,
                                                                 WebRequest webRequest) {
        ErrorDetails errorDetails = new ErrorDetails(
            new Date(),
            exception.getMessage(),
            webRequest.getDescription(false)
        );
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    /**
     * Handles exceptions of type AuthenticationException.
     * This method creates an ErrorDetails object containing the timestamp, exception message,
     * and additional details derived from the WebRequest. The response entity is returned
     * with an HTTP UNAUTHORIZED status.
     *
     * @param exception the AuthenticationException that was thrown
     * @param webRequest the current web request providing context and additional information
     * @return a ResponseEntity containing the ErrorDetails object and an UNAUTHORIZED HTTP status
     */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorDetails> handleAuthenticationException(AuthenticationException exception,
                                                                      WebRequest webRequest) {
        ErrorDetails errorDetails = new ErrorDetails(
                new Date(),
                "Authentication failed: " + exception.getMessage(),
                webRequest.getDescription(false)
        );
        return new ResponseEntity<>(errorDetails, HttpStatus.UNAUTHORIZED);
    }
}