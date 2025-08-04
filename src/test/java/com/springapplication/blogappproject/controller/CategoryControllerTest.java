package com.springapplication.blogappproject.controller;
import java.util.List;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import com.springapplication.blogappproject.exception.ResourceNotFoundException;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import java.lang.String;
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
    /**
     * Tests the getCategory endpoint for an invalid category ID.
     * Ensures that when a non-existent category ID is provided, the endpoint
     * returns a 404 (Not Found) status.
     *
     * @throws Exception if the request processing fails
     */
    @Test
    @WithMockUser(roles = "ADMIN")
    void testGetCategory_ReturnsNotFound_WhenInvalidIdProvided() throws Exception {
        Mockito.when(categoryService.getCategory(99L)).thenThrow(new ResourceNotFoundException("Category", "id", 99L));

        mockMvc.perform(get("/api/categories/99")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
    /**
     * Tests the getCategory endpoint for an unauthenticated user.
     * Ensures that when a user is not authenticated, the endpoint
     * returns a 401 (Unauthorized) status.
     *
     * @throws Exception if the request processing fails
     */
    @Test
    public void testGetCategory_ReturnsUnauthorized_WhenUserIsNotAuthenticated() throws Exception {
        mockMvc.perform(get("/api/categories/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }
    /**
     * Tests the getCategory endpoint for a user without admin privileges.
     * Ensures that when a user without the required role attempts to access a category,
     * the endpoint returns a 403 (Forbidden) status.
     *
     * @throws Exception if the request processing fails
     */
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void testGetCategory_ReturnsForbidden_WhenUserHasNoAdminRole() throws Exception {
        mockMvc.perform(get("/api/categories/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    /**
     * Ensures that sending a null CategoryDto to the addCategory endpoint
     * returns a 400 Bad Request status.
     */
    @Test
    @WithMockUser(roles = "ADMIN")
    void addCategory_ReturnsBadRequest_WhenCategoryDtoIsNull() throws Exception {
        mockMvc.perform(post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(null)))
                .andExpect(status().isBadRequest());
    }

    /**
     * Ensures that requesting a category with an invalid (non-numeric) ID
     * returns a 400 Bad Request status.
     */
    @Test
    @WithMockUser(roles = "ADMIN")
    void getCategory_ReturnsBadRequest_WhenIdIsInvalid() throws Exception {
        mockMvc.perform(get("/api/categories/invalid-id")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    /**
     * Ensures that when no categories exist, the getAllCategories endpoint
     * returns an empty list with a 200 OK status.
     */
    @Test
    @WithMockUser(roles = "ADMIN")
    void getAllCategories_ReturnsEmptyList_WhenNoCategoriesExist() throws Exception {
        Mockito.when(categoryService.getAllCategories()).thenReturn(List.of());

        mockMvc.perform(get("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    /**
     * Ensures that when categories exist, the getAllCategories endpoint
     * returns a list of categories with a 200 OK status.
     */
    @Test
    @WithMockUser(roles = "ADMIN")
    void getAllCategories_ReturnsListOfCategories_WhenCategoriesExist() throws Exception {
        CategoryDto category1 = new CategoryDto(1L, "Tech", "Tech description");
        CategoryDto category2 = new CategoryDto(2L, "Health", "Health description");

        Mockito.when(categoryService.getAllCategories()).thenReturn(List.of(category1, category2));

        mockMvc.perform(get("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Tech"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].name").value("Health"));
    }

    /**
     * Tests the updateCategory endpoint for a valid input.
     * Ensures that when a valid category update is provided by an admin user,
     * the endpoint returns the updated category with a 200 (OK) status.
     *
     * @throws Exception if the request processing fails
     */
    @Test
    @WithMockUser(roles = "ADMIN")
    void updateCategory_ReturnsUpdatedCategory_WhenValidInput() throws Exception {
        CategoryDto updatedCategoryDto = new CategoryDto();
        updatedCategoryDto.setId(1L);
        updatedCategoryDto.setName("Updated Name");
        updatedCategoryDto.setDescription("Updated Description");

        Mockito.when(categoryService.updateCategory(Mockito.any(CategoryDto.class), Mockito.eq(1L)))
                .thenReturn(updatedCategoryDto);

        mockMvc.perform(put("/api/categories/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedCategoryDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(updatedCategoryDto.getId()))
                .andExpect(jsonPath("$.name").value(updatedCategoryDto.getName()))
                .andExpect(jsonPath("$.description").value(updatedCategoryDto.getDescription()));
    }

    /**
     * Tests the updateCategory endpoint for a non-existent category.
     * Ensures that when a category does not exist, the endpoint returns a 404 (Not Found) status.
     *
     * @throws Exception if the request processing fails
     */
    @Test
    @WithMockUser(roles = "ADMIN")
    void updateCategory_ReturnsNotFound_WhenCategoryDoesNotExist() throws Exception {
        CategoryDto updatedCategoryDto = new CategoryDto();
        updatedCategoryDto.setName("Updated Name");
        updatedCategoryDto.setDescription("Updated Description");

        Mockito.when(categoryService.updateCategory(Mockito.any(CategoryDto.class), Mockito.eq(99L)))
                .thenThrow(new ResourceNotFoundException("Category", "id", 99L));

        mockMvc.perform(put("/api/categories/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedCategoryDto)))
                .andExpect(status().isNotFound());
    }

    /**
     * Tests the updateCategory endpoint for invalid input.
     * Ensures that when an invalid category DTO is provided, the endpoint returns a 400 (Bad Request) status.
     *
     * @throws Exception if the request processing fails
     */
    @Test
    @WithMockUser(roles = "ADMIN")
    void updateCategory_ReturnsBadRequest_WhenInputIsInvalid() throws Exception {
        CategoryDto invalidCategoryDto = new CategoryDto(); // Missing required fields

        mockMvc.perform(put("/api/categories/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidCategoryDto)))
                .andExpect(status().isBadRequest());
    }

    /**
     * Tests the updateCategory endpoint for a user without admin privileges.
     * Ensures that when a user with insufficient permissions attempts to update a category,
     * the endpoint returns a 403 (Forbidden) status.
     *
     * @throws Exception if the request processing fails
     */
    @Test
    @WithMockUser(roles = "ADMIN")
    void updateCategory_ReturnsForbidden_WhenUserHasNoAdminRole() throws Exception {
        CategoryDto updatedCategoryDto = new CategoryDto();
        updatedCategoryDto.setName("Updated Name");
        updatedCategoryDto.setDescription("Updated Description");

        mockMvc.perform(put("/api/categories/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedCategoryDto)))
                .andExpect(status().isForbidden());
    }

    /**
     * Tests the updateCategory endpoint for an unauthenticated user.
     * Ensures that when a user is not authenticated, the endpoint returns a 401 (Unauthorized) status.
     *
     * @throws Exception if the request processing fails
     */
    @Test
    public void updateCategory_ReturnsUnauthorized_WhenUserIsNotAuthenticated() throws Exception {
        CategoryDto updatedCategoryDto = new CategoryDto();
        updatedCategoryDto.setName("Updated Name");
        updatedCategoryDto.setDescription("Updated Description");

        mockMvc.perform(put("/api/categories/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedCategoryDto)))
                .andExpect(status().isUnauthorized());
    }

    /**
     * Ensures that when a valid category is provided by an admin user,
     * the addCategory endpoint returns the created category with a 201 (Created) status.
     */
    @Test
    @WithMockUser(roles = "ADMIN")
    void addCategory_ReturnsCreatedStatus_WhenValidInputProvided() throws Exception {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setName("Education");
        categoryDto.setDescription("All about education");

        CategoryDto savedCategory = new CategoryDto();
        savedCategory.setId(3L);
        savedCategory.setName("Education");
        savedCategory.setDescription("All about education");

        Mockito.when(categoryService.addCategory(Mockito.any(CategoryDto.class))).thenReturn(savedCategory);

        mockMvc.perform(post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoryDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(savedCategory.getId()))
                .andExpect(jsonPath("$.name").value(savedCategory.getName()))
                .andExpect(jsonPath("$.description").value(savedCategory.getDescription()));
    }

    /**
     * Ensures that when a valid category update is provided by an admin user,
     * the updateCategory endpoint returns the updated category with a 200 (OK) status.
     */
    @Test
    @WithMockUser(roles = "ADMIN")
    void updateCategory_ReturnsUpdatedCategory_WhenValidInputProvided() throws Exception {
        CategoryDto updatedCategoryDto = new CategoryDto();
        updatedCategoryDto.setId(1L);
        updatedCategoryDto.setName("Updated Name");
        updatedCategoryDto.setDescription("Updated Description");

        Mockito.when(categoryService.updateCategory(Mockito.any(CategoryDto.class), Mockito.eq(1L)))
                .thenReturn(updatedCategoryDto);

        mockMvc.perform(put("/api/categories/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedCategoryDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(updatedCategoryDto.getId()))
                .andExpect(jsonPath("$.name").value(updatedCategoryDto.getName()))
                .andExpect(jsonPath("$.description").value(updatedCategoryDto.getDescription()));
    }

    /**
     * Ensures that when a category exists, the deleteCategory endpoint
     * returns a success message with a 200 (OK) status.
     */
    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteCategory_ReturnsSuccessMessage_WhenCategoryExists() throws Exception {
        Mockito.doNothing().when(categoryService).deleteCategory(1L);

        mockMvc.perform(delete("/api/categories/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Category deleted successfully!"));
    }

    /**
     * Ensures that when a category does not exist, the deleteCategory endpoint
     * returns a 404 (Not Found) status.
     */
    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteCategory_ReturnsNotFound_WhenCategoryDoesNotExist() throws Exception {
        Mockito.doThrow(new ResourceNotFoundException("Category", "id", 99L))
                .when(categoryService).deleteCategory(99L);

        mockMvc.perform(delete("/api/categories/99")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

}
