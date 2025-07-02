package com.springapplication.blogappproject.payload;

import java.util.Date;
/**
 * ErrorDetails represents the structure for error responses in the application.
 * This class encapsulates details about an error, including timestamp, message, and additional details,
 * which are useful for debugging or providing meaningful error information to the client.
 */
public class ErrorDetails {

    /**
     * The timestamp indicating the specific date and time when an error occurred.
     * This field provides a precise moment reference for logging and debugging purposes.
     */
    private Date timestamp;
    /**
     * Represents a descriptive message associated with an error or event.
     * This field is intended to store a human-readable explanation or additional context
     * to provide clarity regarding a specific error or situation.
     */
    private String message;
    /**
     * Represents additional details about an error.
     * This field provides contextual or supplementary information
     * regarding the specific error that occurred.
     */
    private String details;

    /**
     * Constructor to initialize ErrorDetails with provided parameters.
     *
     * @param timestamp the timestamp indicating when the error occurred
     * @param message   the message providing a brief description of the error
     * @param details   additional details providing more context about the error
     */
    public ErrorDetails(Date timestamp, String message, String details) {
        super();
        this.timestamp = timestamp;
        this.message = message;
        this.details = details;
    }

    /**
     * Retrieves the timestamp indicating when the error occurred.
     *
     * @return the timestamp as a Date object
     */
    public Date getTimestamp() {
        return timestamp;
    }

    /**
     * Retrieves the error message associated with this instance.
     *
     * @return the error message as a string
     */
    public String getMessage() {
        return message;
    }

    /**
     * Retrieves the detailed information about the error.
     *
     * @return a string representing the error details.
     */
    public String getDetails() {
        return details;
    }
}
