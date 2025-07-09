package com.springapplication.blogappproject.exception;

import com.springapplication.blogappproject.payload.ErrorDetails;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.validation.FieldError;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

/**
 * Test class for the {@link GlobalExceptionHandler}.
 * This class contains test methods to ensure the exception handling functionality of the {@code GlobalExceptionHandler}
 * is working correctly by verifying responses for specific scenarios.
 *
 * The test methods focus on validating:
 * - The construction of response entities for specific types of exceptions.
 * - Correct population of error details like timestamp, message, and additional context.
 * - Proper mapping of HTTP status codes to the appropriate exceptions.
 */
@SpringBootTest
public class GlobalExceptionHandlerTest {

    /**
     * The {@code globalExceptionHandler} is an instance of the {@link GlobalExceptionHandler} class.
     * It is autowired to the test class to facilitate testing of the centralized exception handling logic.
     * This component provides methods for handling exceptions such as {@link ResourceNotFoundException},
     * {@link BlogAPIException}, {@link AccessDeniedException}, and other global exceptions.
     *
     * The usage of {@code globalExceptionHandler} ensures:
     * - Consistent and appropriate HTTP responses for various exceptions.
     * - Simplified access to the application's exception-handling mechanisms within test cases.
     *
     * It allows verification of:
     * - Response entities generated for specific exceptions.
     * - The correctness of HTTP status codes mapped to handled exceptions.
     * - Proper population of error details including timestamp, message, and additional context.
     */
    @Autowired
    private GlobalExceptionHandler globalExceptionHandler;

    /**
     * Tests the behavior of the {@code handleResourceNotFoundException} method in the {@code GlobalExceptionHandler}.
     * This test method verifies that the appropriate error response is returned when a {@code ResourceNotFoundException}
     * is thrown during request processing.
     *
     * The test validates the following:
     * - The HTTP status in the response is {@code HttpStatus.NOT_FOUND}.
     * - The response body contains correct error details, including a properly formatted message and request-specific context.
     * - The error timestamp is generated correctly and is either equal to or earlier than the current time.
     *
     * The scenario simulated in this test involves a missing "Post" resource identified by its "id" field.
     */
    @Test
    public void testHandleResourceNotFoundException() {
        // Arrange
        String resourceName = "Post";
        String fieldName = "id";
        long fieldValue = 1L;
        String expectedMessage = String.format("%s not found with %s : '%s'", resourceName, fieldName, fieldValue);
        ResourceNotFoundException exception = new ResourceNotFoundException(resourceName, fieldName, fieldValue);

        WebRequest webRequest = Mockito.mock(WebRequest.class);
        String requestDescription = "uri=/api/posts/1";
        when(webRequest.getDescription(false)).thenReturn(requestDescription);

        // Act
        ResponseEntity<ErrorDetails> response = globalExceptionHandler.handleResourceNotFoundException(exception, webRequest);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        ErrorDetails errorDetails = response.getBody();
        assert errorDetails != null;
        assertEquals(expectedMessage, errorDetails.getMessage());
        assertEquals(requestDescription, errorDetails.getDetails());
        assert errorDetails.getTimestamp().before(new Date()) || errorDetails.getTimestamp().equals(new Date());
    }


}