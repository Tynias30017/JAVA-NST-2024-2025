package org.example.projectmanagerapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.projectmanagerapp.entity.project.Project;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class ProjectControllerIntegrationTest {
    @Autowired
    MockMvc mvc;
    @Autowired
    ObjectMapper mapper;

    @Test
    void shouldReturn404WhenProjectNotFound() throws Exception {
        mvc.perform(get("/api/projects/999999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturn404WhenDeleteProjectNotFound() throws Exception {
        mvc.perform(delete("/api/projects/999999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturn404WhenUpdateProjectNotFound() throws Exception {
        Project project = new Project();
        project.setName("test");
        project.setDescription("desc");
        String json = mapper.writeValueAsString(project);
        mvc.perform(put("/api/projects/999999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturn400WhenCreateProjectWithInvalidData() throws Exception {
        Project project = new Project(); // brak wymaganych pól
        String json = mapper.writeValueAsString(project);
        mvc.perform(post("/api/projects")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn404WhenAssignUserToNonExistingProject() throws Exception {
        mvc.perform(put("/api/projects/999999/users/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturn404WhenAssignNonExistingUserToProject() throws Exception {
        // Najpierw utwórz projekt
        Project project = new Project();
        project.setName("test");
        project.setDescription("desc");
        String json = mapper.writeValueAsString(project);
        String resp = mvc.perform(post("/api/projects")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        Long projectId = mapper.readValue(resp, Project.class).getId();
        mvc.perform(put("/api/projects/" + projectId + "/users/999999"))
                .andExpect(status().isNotFound());
    }
}

