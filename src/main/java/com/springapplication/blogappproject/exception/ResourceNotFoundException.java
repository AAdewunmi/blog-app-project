package com.springapplication.blogappproject.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Custom exception to handle resource not found scenarios.
 * This exception is thrown when a requested resource (like a Post) is not found in the database.
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {
    private String resourceName;
    private String fieldName;
    private long fieldValue;

    private static final long serialVersionUID = 1L;
    /**
     * Constructs a new ResourceNotFoundException with the specified resource name, field name, and field value.
     *
     * @param resourceName the name of the resource that was not found
     * @param fieldName    the name of the field that was used to search for the resource
     * @param fieldValue   the value of the field that was used to search for the resource
     */
    public ResourceNotFoundException(String resourceName, String fieldName, long fieldValue) {
        super(String.format("%s not found with %s : '%s'", resourceName, fieldName, fieldValue));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

    /**
     * Constructs a new ResourceNotFoundException with the specified resource name and field name.
     *
     * @param resourceName the name of the resource that was not found
     * @param fieldName    the name of the field that was used to search for the resource
     */

    public String getResourceName() {
        return resourceName;
    }

    /**
     * Constructs a new ResourceNotFoundException with the specified resource name and field name.
     *
     * @param resourceName the name of the resource that was not found
     * @param fieldName    the name of the field that was used to search for the resource
     */

    public String getFieldName() {
        return fieldName;
    }

    /**
     * Constructs a new ResourceNotFoundException with the specified resource name, field name, and field value.
     *
     * @param resourceName the name of the resource that was not found
     * @param fieldName    the name of the field that was used to search for the resource
     * @param fieldValue   the value of the field that was used to search for the resource
     */

    public long getFieldValue() {
        return fieldValue;
    }

}
