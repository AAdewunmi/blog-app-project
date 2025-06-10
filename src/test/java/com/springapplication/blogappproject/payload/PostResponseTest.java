package com.springapplication.blogappproject.payload;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link PostResponse} class.
 * This test class verifies the functionality of the PostResponse DTO,
 * including its constructors, getters, setters, and Lombok-generated methods.
 *
 * @author author
 * @version 1.0
 * @see PostResponse
 * @see PostDto
 */
class PostResponseTest {
    /**
     * Default page number used in tests
     */
    private static final int PAGE_NUMBER = 0;
    /**
     * Default page size used in tests
     */
    private static final int PAGE_SIZE = 10;
    /**
     * Default total elements count used in tests
     */
    private static final long TOTAL_ELEMENTS = 20;
    /**
     * Default total pages count used in tests
     */
    private static final int TOTAL_PAGES = 2;
    /**
     * Default last page flag used in tests
     */
    private static final boolean LAST_PAGE = false;

    /**
     * The PostResponse instance being tested
     */
    private PostResponse postResponse;
    /**
     * Test content list containing PostDto objects
     */
    private List<PostDto> content;

    /**
     * Sets up the test environment before each test.
     * Initializes a PostResponse instance with test data.
     */
    @BeforeEach
    void setUp() {
        content = new ArrayList<>();
        content.add(new PostDto(1L, "First Post", "Content 1", "Description 1"));
        content.add(new PostDto(2L, "Second Post", "Content 2", "Description 2"));

        postResponse = new PostResponse(
                content,
                PAGE_NUMBER,
                PAGE_SIZE,
                TOTAL_ELEMENTS,
                TOTAL_PAGES,
                LAST_PAGE
        );
    }

    /**
     * Tests the no-args constructor of PostResponse.
     * Verifies that all fields are initialized to their default values.
     */
    @Test
    @DisplayName("Should create PostResponse using no-args constructor")
    void shouldCreatePostResponseUsingNoArgsConstructor() {
        PostResponse response = new PostResponse();

        assertAll("PostResponse initialization",
                () -> assertNull(response.getContent(), "Content should be null"),
                () -> assertEquals(0, response.getPageNumber(), "Page number should be 0"),
                () -> assertEquals(0, response.getPageSize(), "Page size should be 0"),
                () -> assertEquals(0, response.getTotalElements(), "Total elements should be 0"),
                () -> assertEquals(0, response.getTotalPages(), "Total pages should be 0"),
                () -> assertFalse(response.isLastPage(), "Last page should be false")
        );
    }

    /**
     * Tests the all-args constructor of PostResponse.
     * Verifies that all fields are correctly initialized with provided values.
     */
    @Test
    @DisplayName("Should create PostResponse using all-args constructor")
    void shouldCreatePostResponseUsingAllArgsConstructor() {
        assertAll("PostResponse all-args constructor",
                () -> assertEquals(content, postResponse.getContent(), "Content should match"),
                () -> assertEquals(PAGE_NUMBER, postResponse.getPageNumber(), "Page number should match"),
                () -> assertEquals(PAGE_SIZE, postResponse.getPageSize(), "Page size should match"),
                () -> assertEquals(TOTAL_ELEMENTS, postResponse.getTotalElements(), "Total elements should match"),
                () -> assertEquals(TOTAL_PAGES, postResponse.getTotalPages(), "Total pages should match"),
                () -> assertEquals(LAST_PAGE, postResponse.isLastPage(), "Last page flag should match")
        );
    }

    /**
     * Tests the content getter and setter methods.
     * Verifies that content can be updated and retrieved correctly.
     */
    @Test
    @DisplayName("Should set and get content correctly")
    void shouldSetAndGetContent() {
        List<PostDto> newContent = List.of(new PostDto(3L, "New Post", "New Content", "New Description"));
        postResponse.setContent(newContent);

        assertAll("Content operations",
                () -> assertEquals(newContent, postResponse.getContent(), "Content should be updated"),
                () -> assertEquals(1, postResponse.getContent().size(), "Content size should match"),
                () -> assertEquals("New Post", postResponse.getContent().get(0).getTitle(), "Post title should match")
        );
    }

    /**
     * Tests the pagination property getter and setter methods.
     * Verifies that all pagination-related fields can be updated and retrieved correctly.
     */
    @Test
    @DisplayName("Should set and get pagination properties")
    void shouldSetAndGetPaginationProperties() {
        postResponse.setPageNumber(1);
        postResponse.setPageSize(5);
        postResponse.setTotalElements(15);
        postResponse.setTotalPages(3);
        postResponse.setLastPage(true);

        assertAll("Pagination properties",
                () -> assertEquals(1, postResponse.getPageNumber(), "Page number should be updated"),
                () -> assertEquals(5, postResponse.getPageSize(), "Page size should be updated"),
                () -> assertEquals(15, postResponse.getTotalElements(), "Total elements should be updated"),
                () -> assertEquals(3, postResponse.getTotalPages(), "Total pages should be updated"),
                () -> assertTrue(postResponse.isLastPage(), "Last page should be updated")
        );
    }
}