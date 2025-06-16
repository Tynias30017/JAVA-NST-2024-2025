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
                .andExpect(jsonPath("$.name", is("testuser")))
                .andReturn().getResponse().getContentAsString();

        Long userId = mapper.readValue(userResp, User.class).getId();
        Assertions.assertNotNull(userId, "User ID should not be null after creation.");
    }

    @Test
    @Order(2)
    void shouldGetUser() throws Exception {
        // Tworzenie użytkownika na potrzeby testu
        User user = new User();
        user.setName("testuser");
        String userJson = mapper.writeValueAsString(user);
        String userResp = mvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson))
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();
        Long userId = mapper.readValue(userResp, User.class).getId();
        mvc.perform(get("/api/users/" + userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userId.intValue())))
                .andExpect(jsonPath("$.name", is("testuser")));
    }

    @Test
    @Order(3)
    void shouldUpdateUser() throws Exception {
        User user = new User();
        user.setName("testuser");
        String userJson = mapper.writeValueAsString(user);
        String userResp = mvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson))
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();
        Long userId = mapper.readValue(userResp, User.class).getId();
        User updatedUser = new User();
        updatedUser.setName("updateduser");
        String updatedUserJson = mapper.writeValueAsString(updatedUser);
        mvc.perform(put("/api/users/" + userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedUserJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("updateduser")));
    }

    @Test
    @Order(4)
    void shouldCreateProject() throws Exception {
        Project project = new Project();
        project.setName("proj");
        project.setDescription("desc");
        String projectJson = mapper.writeValueAsString(project);

        String projResp = mvc.perform(post("/api/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(projectJson))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Long projectId = mapper.readValue(projResp, Project.class).getId();
        Assertions.assertNotNull(projectId);
    }

    @Test
    @Order(5)
    void shouldGetProject() throws Exception {
        Project project = new Project();
        project.setName("proj");
        project.setDescription("desc");
        String projectJson = mapper.writeValueAsString(project);
        String projResp = mvc.perform(post("/api/projects")
                .contentType(MediaType.APPLICATION_JSON)
                .content(projectJson))
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();
        Long projectId = mapper.readValue(projResp, Project.class).getId();
        mvc.perform(get("/api/projects/" + projectId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(projectId.intValue())))
                .andExpect(jsonPath("$.description", is("desc")));
    }

    @Test
    @Order(6)
    void shouldUpdateProject() throws Exception {
        Project project = new Project();
        project.setName("proj");
        project.setDescription("desc");
        String projectJson = mapper.writeValueAsString(project);
        String projResp = mvc.perform(post("/api/projects")
                .contentType(MediaType.APPLICATION_JSON)
                .content(projectJson))
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();
        Long projectId = mapper.readValue(projResp, Project.class).getId();
        Project updatedProject = new Project();
        updatedProject.setName("updatedproj");
        String updatedProjectJson = mapper.writeValueAsString(updatedProject);
        mvc.perform(put("/api/projects/" + projectId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedProjectJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("updatedproj")));
    }

    @Test
    @Order(7)
    void shouldAssignUserToProject() throws Exception {
        // Tworzenie użytkownika i projektu na potrzeby testu
        User user = new User();
        user.setName("testuser");
        String userJson = mapper.writeValueAsString(user);
        String userResp = mvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson))
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();
        Long userId = mapper.readValue(userResp, User.class).getId();
        Project project = new Project();
        project.setName("proj");
        project.setDescription("desc");
        String projectJson = mapper.writeValueAsString(project);
        String projResp = mvc.perform(post("/api/projects")
                .contentType(MediaType.APPLICATION_JSON)
                .content(projectJson))
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();
        Long projectId = mapper.readValue(projResp, Project.class).getId();
        mvc.perform(put("/api/projects/" + projectId + "/users/" + userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.users", hasSize(1)))
                .andExpect(jsonPath("$.users[0].id", is(userId.intValue())));
    }


    @Test
    @Order(9)
    void shouldDeleteProject() throws Exception {
        Project project = new Project();
        project.setName("toDelete");
        project.setDescription("desc");
        String projectJson = mapper.writeValueAsString(project);
        String projResp = mvc.perform(post("/api/projects")
                .contentType(MediaType.APPLICATION_JSON)
                .content(projectJson))
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();
        Long tempProjectId = mapper.readValue(projResp, Project.class).getId();
        mvc.perform(get("/api/projects/" + tempProjectId))
                .andExpect(status().isOk());
        mvc.perform(delete("/api/projects/" + tempProjectId))
                .andExpect(status().isOk());
        mvc.perform(get("/api/projects/" + tempProjectId))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(8)
    void shouldDeleteUser() throws Exception {
        User user = new User();
        user.setName("toDelete");
        String userJson = mapper.writeValueAsString(user);
        String userResp = mvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson))
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();
        Long tempUserId = mapper.readValue(userResp, User.class).getId();
        mvc.perform(get("/api/users/" + tempUserId))
                .andExpect(status().isOk());
        mvc.perform(get("/api/projects")).andExpect(status().isOk())
            .andDo(result -> {
                Project[] projects = mapper.readValue(result.getResponse().getContentAsString(), Project[].class);
                for (Project p : projects) {
                    try {
                        mvc.perform(put("/api/projects/" + p.getId() + "/users/" + tempUserId + "/remove"))
                            .andExpect(status().isOk());
                    } catch (Exception e) {
                    }
                }
            });
        mvc.perform(delete("/api/users/" + tempUserId))
                .andExpect(status().isOk());
        mvc.perform(get("/api/users/" + tempUserId))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(10)
    void shouldAssignUserToProjectAndVerifyRelation() throws Exception {
        User user = new User();
        user.setName("integrationUser");
        String userJson = mapper.writeValueAsString(user);
        String userResp = mvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson))
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();
        Long userId = mapper.readValue(userResp, User.class).getId();

        Project project = new Project();
        project.setName("integrationProject");
        project.setDescription("desc");
        String projectJson = mapper.writeValueAsString(project);
        String projResp = mvc.perform(post("/api/projects")
                .contentType(MediaType.APPLICATION_JSON)
                .content(projectJson))
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();
        Long projectId = mapper.readValue(projResp, Project.class).getId();

        mvc.perform(post("/api/projects/" + projectId + "/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("[" + userId + "]"))
            .andExpect(status().isOk());

        mvc.perform(get("/api/projects/" + projectId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.users[*].id", hasItem(userId.intValue())))
            .andExpect(jsonPath("$.users[*].name", hasItem("integrationUser")));
    }

}