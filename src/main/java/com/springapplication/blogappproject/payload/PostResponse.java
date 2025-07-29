package com.springapplication.blogappproject.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Represents the response object for paginated blog posts.
 * This class contains metadata about the pagination and the list of blog posts.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostResponse {
    /**
     * Represents the list of PostDto objects that contain details about blog posts.
     * This field is used to store the content of the current page in a paginated response.
     */
    private List<PostDto> content;
    /**
     * The current page number in a paginated response.
     * This field represents the index of the page being returned in the response,
     * commonly used in pagination implementations to indicate the current subset of data.
     */
    private int pageNumber;
    /**
     * Represents the number of items to be included on a single page of the paginated response.
     * This field is used to determine the size of the page when fetching a subset of data.
     */
    private int pageSize;
    /**
     * Represents the total number of elements in a paginated response.
     * This field indicates the total count of items available across all pages.
     */
    private long totalElements;
    /**
     *
     */
    private int totalPages;
    /**
     * Indicates whether the current page is the last page in the pagination sequence.
     * If true, it signifies that there are no more pages after the current one.
     */
    private boolean lastPage;
}
