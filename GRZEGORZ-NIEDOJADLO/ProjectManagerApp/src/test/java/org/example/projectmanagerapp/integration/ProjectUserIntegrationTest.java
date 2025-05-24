package org.example.projectmanagerapp.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.projectmanagerapp.entity.project.Project;
import org.example.projectmanagerapp.entity.user.User;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProjectUserIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void setDatasourceProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper mapper;

    static Long userId;
    static Long projectId;

    @Test
    @Order(1)
    void shouldCreateUser() throws Exception {
        User user = new User();
        user.setName("testuser");
        String userJson = mapper.writeValueAsString(user);

        String userResp = mvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is("testuser")))
                .andReturn().getResponse().getContentAsString();

        userId = mapper.readValue(userResp, User.class).getId();
        Assertions.assertNotNull(userId);
    }

    @Test
    @Order(2)
    void shouldGetUser() throws Exception {
        mvc.perform(get("/api/users/" + userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userId.intValue())))
                .andExpect(jsonPath("$.username", is("testuser")));
    }

    @Test
    @Order(3)
    void shouldUpdateUser() throws Exception {
        User updatedUser = new User();
        updatedUser.setName("updateduser");
        String userJson = mapper.writeValueAsString(updatedUser);

        mvc.perform(put("/api/users/" + userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is("updateduser")));
    }

    @Test
    @Order(4)
    void shouldCreateProject() throws Exception {
        Project project = new Project();
        project.setName("proj");
        String projectJson = mapper.writeValueAsString(project);

        String projResp = mvc.perform(post("/api/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(projectJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("proj")))
                .andExpect(jsonPath("$.description", is("desc")))
                .andReturn().getResponse().getContentAsString();

        projectId = mapper.readValue(projResp, Project.class).getId();
        Assertions.assertNotNull(projectId);
    }

    @Test
    @Order(5)
    void shouldGetProject() throws Exception {
        mvc.perform(get("/api/projects/" + projectId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(projectId.intValue())))
                .andExpect(jsonPath("$.name", is("proj")))
                .andExpect(jsonPath("$.description", is("desc")));
    }

    @Test
    @Order(6)
    void shouldUpdateProject() throws Exception {
        Project updatedProject = new Project();
        updatedProject.setName("updatedproj");
        String projectJson = mapper.writeValueAsString(updatedProject);

        mvc.perform(put("/api/projects/" + projectId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(projectJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("updatedproj")))
                .andExpect(jsonPath("$.description", is("updateddesc")));
    }

    @Test
    @Order(7)
    void shouldAssignUserToProject() throws Exception {
        mvc.perform(put("/api/projects/" + projectId + "/users/" + userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.users", hasSize(1)))
                .andExpect(jsonPath("$.users[0].id", is(userId.intValue())));
    }

    @Test
    @Order(8)
    void shouldDeleteUser() throws Exception {
        mvc.perform(delete("/api/users/" + userId))
                .andExpect(status().isOk());

        mvc.perform(get("/api/users/" + userId))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(9)
    void shouldDeleteProject() throws Exception {
        mvc.perform(delete("/api/projects/" + projectId))
                .andExpect(status().isOk());

        mvc.perform(get("/api/projects/" + projectId))
                .andExpect(status().isNotFound());
    }
}