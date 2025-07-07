package com.springapplication.blogappproject.exception;

import com.springapplication.blogappproject.payload.ErrorDetails;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
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
     * Handles the ResourceNotFoundException by constructing a response entity encapsulating error details.
     * This method is triggered when a ResourceNotFoundException is thrown, and it generates
     * a standardized error response with details such as timestamp, message, and additional information.
     *
     * @param exception the ResourceNotFoundException that was thrown when a requested resource could not be found
     * @param webRequest the current web request, used to extract additional context or description of the request
     * @return a ResponseEntity containing ErrorDetails and an HTTP status of NOT_FOUND
     */
    // handle specific exceptions
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorDetails> handleResourceNotFoundException(ResourceNotFoundException exception,
                                                                        WebRequest webRequest){
        ErrorDetails errorDetails = new ErrorDetails(new Date(), exception.getMessage(),
                webRequest.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
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
 * Handles the validation errors for method arguments annotated with validation constraints.
 * This method is invoked when a MethodArgumentNotValidException is thrown, typically during
 * validation of request body fields in REST APIs.
 *
 * @param ex the MethodArgumentNotValidException containing details about the validation errors
 * @param headers the HTTP headers associated with the request
 * @param status the HTTP status code determined by the response status
 * @param request the WebRequest object that encapsulates details of the web request
 * @return a ResponseEntity object containing error details (timestamp, generic error message, and validation errors)
 *         with a BAD_REQUEST HTTP status
 */

//@Override
//protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
//                                                              HttpHeaders headers,
//                                                              HttpStatusCode status,
//                                                              WebRequest request) {
//    Map<String, String> errors = new HashMap<>();
//    ex.getBindingResult().getAllErrors().forEach((error) -> {
//        String fieldName;
//        String message;
//
//        if (error instanceof FieldError) {
//            FieldError fieldError = (FieldError) error;
//            fieldName = fieldError.getField();
//            message = fieldError.getDefaultMessage();
//        } else {
//            fieldName = error.getObjectName();
//            message = error.getDefaultMessage();
//        }
//
//        errors.put(fieldName, message);
//    });
//
//    ErrorDetails errorDetails = new ErrorDetails(
//            new Date(),
//            "Validation failed",
//            errors.toString()
//    );
//
//    return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
//}


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
     * Handles the jakarta.validation.ConstraintViolationException by constructing a response entity
     * encapsulating error details about the validation failures.
     * This method is triggered when a ConstraintViolationException is thrown, collecting validation
     * errors and providing a standardized error response with details such as timestamp, message,
     * and validation-specific information.
     *
     * @param ex the ConstraintViolationException containing details about the validation failures
     * @param request the WebRequest object that encapsulates details of the web request
     * @return a ResponseEntity containing ErrorDetails with a validation failure message, specific
     *         validation errors, and an HTTP status of BAD_REQUEST
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDetails> handleValidationExceptions(
            MethodArgumentNotValidException ex,
            WebRequest request) {

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            // Add debugging information
            System.out.println("Field: " + fieldName);
            System.out.println("Value: " + ((FieldError) error).getRejectedValue());
            System.out.println("Message: " + errorMessage);

            errors.put(fieldName, errorMessage);
        });

        ErrorDetails errorDetails = new ErrorDetails(
                new Date(),
                "Validation failed",
                errors.toString()
        );

        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }


    /**
     * Handles validation failures during method argument validation in the request body.
     * This method is triggered when a {@code MethodArgumentNotValidException} is thrown,
     * capturing validation errors from the {@code BindingResult}, and constructing a
     * detailed error response in a standardized format.
     *
     * @param ex the exception thrown when a method argument is deemed invalid
     * @param headers the HTTP headers of the request
     * @param status the HTTP status code of the failed request
     * @param request the web request context in which the exception occurred
     * @return a {@code ResponseEntity} containing the error details and an HTTP status of BAD_REQUEST
     */
    // Add this method as well
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach((error) -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });

        ErrorDetails errorDetails = new ErrorDetails(
                new Date(),
                "Validation failed",
                errors.toString()
        );

        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }



}