package com.springapplication.blogappproject.payload;

import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ErrorDetailsTest {

    /**
     * Tests that the `getTimestamp` method of the `ErrorDetails` class
     * returns the correct timestamp value provided during the object's initialization.
     *
     * This test verifies the following:
     * - The timestamp retrieved using the `getTimestamp` method matches the expected timestamp.
     * - Ensures the intended behavior of the `getTimestamp` method when a valid timestamp is passed in the constructor.
     *
     * Steps:
     * 1. Arrange: Create an `ErrorDetails` instance with a specific timestamp.
     * 2. Act: Retrieve the timestamp using the `getTimestamp` method.
     * 3. Assert: Compare the retrieved timestamp with the expected timestamp for equality.
     */

    @Test
    void testGetTimestampReturnsCorrectValue() {
        // Arrange
        Date expectedTimestamp = new Date();
        ErrorDetails errorDetails = new ErrorDetails(expectedTimestamp, "Test message", "Test details");

        // Act
        Date actualTimestamp = errorDetails.getTimestamp();

        // Assert
        assertEquals(expectedTimestamp, actualTimestamp, "The getTimestamp method should return the correct timestamp.");
    }
}