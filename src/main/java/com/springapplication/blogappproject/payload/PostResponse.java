package com.springapplication.blogappproject.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
/** Represents the response containing a list of posts and pagination details.
 * This class is used to encapsulate the response data for post-retrieval operations.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostResponse {
    private List<PostDto> content;
    private int pageNumber;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean lastPage;
}
