package com.springapplication.blogappproject.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public class BlogAPIException extends RuntimeException{

    @Getter
    private HttpStatus httpStatus;
    private String message;
    /**
     * Constructs a new BlogAPIException with the specified HTTP status and message.
     *
     * @param httpStatus the HTTP status associated with the exception
     * @param message the detail message of the exception
     */
    public BlogAPIException(HttpStatus httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
        this.message = message;
    }
    /**
     * Constructs a new BlogAPIException with the specified message, HTTP status, and additional message.
     *
     * @param message the detail message of the exception
     * @param httpStatus the HTTP status associated with the exception
     * @param message1 an additional message for the exception
     */
    public BlogAPIException(String message, HttpStatus httpStatus, String message1) {
        super(message);
        this.httpStatus = httpStatus;
        this.message = message1;
    }
    /**
     * Overrides the getMessage method to return the custom message.
     *
     * @return the detail message of the exception
     */
    @Override
    public String getMessage() {
        return message;
    }

}
