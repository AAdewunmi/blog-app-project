package com.springapplication.blogappproject.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springapplication.blogappproject.config.TestSecurityConfig;
import com.springapplication.blogappproject.payload.CategoryDto;
import com.springapplication.blogappproject.service.CategoryService;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Import(TestSecurityConfig.class) // Ensure this config restricts /api/categories to ADMIN
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
/** * Test class for the CategoryController.
 * This class contains tests for the addCategory endpoint, ensuring that it behaves correctly
 * under different user roles and authentication states.
 */
public class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;

    @Autowired
    private ObjectMapper objectMapper;
    /**
     * Tests the addCategory endpoint for a valid input.
     * Ensures that when a valid category is provided by an admin user, the endpoint
     * returns the created category with a 201 (Created) status.
     *
     * @throws Exception if the request processing fails
     */
    @Test
    @WithMockUser(roles = "ADMIN")
    public void addCategory_ShouldReturnCreatedCategory_WhenValidInput() throws Exception {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setName("Technology");
        categoryDto.setDescription("All about technology");

        CategoryDto savedCategory = new CategoryDto();
        savedCategory.setId(1L);
        savedCategory.setName("Technology");
        savedCategory.setDescription("All about technology");

        Mockito.when(categoryService.addCategory(Mockito.any(CategoryDto.class))).thenReturn(savedCategory);

        mockMvc.perform(post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoryDto)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(savedCategory.getId()))
                .andExpect(jsonPath("$.name").value(savedCategory.getName()))
                .andExpect(jsonPath("$.description").value(savedCategory.getDescription()));
    }
    /**
     * Tests the addCategory endpoint for an unauthenticated user.
     * Ensures that when a user is not authenticated, the endpoint
     * returns a 401 (Unauthorized) status.
     *
     * @throws Exception if the request processing fails
     */
    @Test
    public void addCategory_ShouldReturnUnauthorized_WhenUserIsNotAuthenticated() throws Exception {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setName("Technology");
        categoryDto.setDescription("All about technology");

        mockMvc.perform(post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoryDto)))
                .andExpect(status().isUnauthorized());
    }
    /**
     * Tests the addCategory endpoint for a user without admin privileges.
     * Ensures that when a user with insufficient permissions attempts to add a category,
     * the endpoint returns a 403 (Forbidden) status.
     *
     * @throws Exception if the request processing fails
     */
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void addCategory_ShouldReturnForbidden_WhenUserHasNoAdminRole() throws Exception {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setName("Technology");
        categoryDto.setDescription("All about technology");

        mockMvc.perform(post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoryDto)))
                .andExpect(status().isForbidden());
    }
    /**
     * Tests the addCategory endpoint for an invalid category input.
     * Ensures that when an invalid category DTO is provided, the endpoint
     * returns a 400 (Bad Request) status.
     *
     * @throws Exception if the request processing fails
     */
    @Test
    @WithMockUser(roles = "ADMIN")
    void testAddCategory_ReturnsBadRequest_WhenCategoryDtoIsInvalid() throws Exception {
        CategoryDto invalidCategoryDto = new CategoryDto(); // Missing required fields

        mockMvc.perform(post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidCategoryDto)))
                .andExpect(status().isBadRequest());
    }
    /**
     * Tests the getCategory endpoint for a valid category ID.
     * Ensures that when a valid category ID is provided, the endpoint
     * returns the category details with a 200 (OK) status.
     *
     * @throws Exception if the request processing fails
     */
    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetCategory_ReturnsCategory_WhenValidIdProvided() throws Exception {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(1L);
        categoryDto.setName("Technology");
        categoryDto.setDescription("All about technology");

        Mockito.when(categoryService.getCategory(1L)).thenReturn(categoryDto);

        mockMvc.perform(get("/api/categories/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(categoryDto.getId()))
                .andExpect(jsonPath("$.name").value(categoryDto.getName()))
                .andExpect(jsonPath("$.description").value(categoryDto.getDescription()));
    }
}
