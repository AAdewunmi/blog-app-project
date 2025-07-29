package com.springapplication.blogappproject.utils;
/*
 * AppConstants is a utility class that holds application-wide constants.
 * It is currently empty but can be used to define constants used throughout the application.
 */
public class AppConstants {

    /**
     * The default page number to be used for pagination when no specific page number is provided.
     * Represents the first page in a paginated response.
     */
    public static final String DEFAULT_PAGE_NUMBER = "0";

    /**
     * The default page size to be used for pagination when no specific size is provided.
     * Indicates the number of items per page in a paginated response.
     */
    public static final String DEFAULT_PAGE_SIZE = "10";

    /**
     * The default field by which results should be sorted when no specific sort field is provided.
     * Typically represents the primary identifier of an entity.
     */
    public static final String DEFAULT_SORT_BY = "id";

    /**
     * The default sort direction to be used when no specific direction is provided.
     * "asc" indicates ascending order.
     */
    public static final String DEFAULT_SORT_DIR = "asc";

}
