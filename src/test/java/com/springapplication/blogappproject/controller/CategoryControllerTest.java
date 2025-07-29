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
public class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;

    @Autowired
    private ObjectMapper objectMapper;

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
}
