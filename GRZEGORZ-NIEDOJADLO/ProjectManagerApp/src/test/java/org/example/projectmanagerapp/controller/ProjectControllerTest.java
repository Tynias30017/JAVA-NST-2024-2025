package org.example.projectmanagerapp.controller;

import org.example.projectmanagerapp.entity.project.Project;
import org.example.projectmanagerapp.repository.ProjectRepository;
import org.example.projectmanagerapp.repository.TaskRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ProjectControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ProjectRepository projectRepository;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private TaskRepository taskRepository;

    @BeforeEach
    void clearDb() {
        taskRepository.deleteAll();
        projectRepository.deleteAll();
    }

    @Test
    void shouldCreateAndFetchProject() throws Exception {
        Project project = new Project();
        project.setName("Projekt A");

        mockMvc.perform(post("/api/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(project)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Projekt A"));

        mockMvc.perform(get("/api/projects"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Projekt A"));
    }

    @Test
    void shouldReturnNotFoundForNonExistingProject() throws Exception {
        mockMvc.perform(get("/api/projects/99999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnBadRequestForInvalidProjectCreation() throws Exception {
        Project project = new Project(); // brak nazwy
        mockMvc.perform(post("/api/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(project)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldUpdateProject() throws Exception {
        Project project = new Project();
        project.setName("Projekt B");
        String projectJson = objectMapper.writeValueAsString(project);
        String response = mockMvc.perform(post("/api/projects")
                .contentType(MediaType.APPLICATION_JSON)
                .content(projectJson))
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();
        Long id = objectMapper.readValue(response, Project.class).getId();

        Project updated = new Project();
        updated.setName("Projekt B2");
        String updatedJson = objectMapper.writeValueAsString(updated);
        mockMvc.perform(put("/api/projects/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(updatedJson))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("Projekt B2"));
    }

    @Test
    void shouldDeleteProject() throws Exception {
        Project project = new Project();
        project.setName("Projekt C");
        String projectJson = objectMapper.writeValueAsString(project);
        String response = mockMvc.perform(post("/api/projects")
                .contentType(MediaType.APPLICATION_JSON)
                .content(projectJson))
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();
        Long id = objectMapper.readValue(response, Project.class).getId();

        mockMvc.perform(delete("/api/projects/" + id))
            .andExpect(status().isOk());
        mockMvc.perform(get("/api/projects/" + id))
            .andExpect(status().isNotFound());
    }

    @Test
    void shouldAssignUserToProject() throws Exception {
        String userJson = "{\"name\":\"Jan Nowak\"}";
        String userResp = mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson))
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();
        Long userId = objectMapper.readTree(userResp).get("id").asLong();

        Project project = new Project();
        project.setName("Projekt D");
        String projectJson = objectMapper.writeValueAsString(project);
        String projResp = mockMvc.perform(post("/api/projects")
                .contentType(MediaType.APPLICATION_JSON)
                .content(projectJson))
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();
        Long projectId = objectMapper.readTree(projResp).get("id").asLong();

        mockMvc.perform(put("/api/projects/" + projectId + "/users/" + userId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.users[0].id").value(userId.intValue()));
    }

    @Test
    void shouldRemoveUserFromProject() throws Exception {
        String userJson = "{\"name\":\"Anna Kowalska\"}";
        String userResp = mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson))
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();
        Long userId = objectMapper.readTree(userResp).get("id").asLong();

        Project project = new Project();
        project.setName("Projekt E");
        String projectJson = objectMapper.writeValueAsString(project);
        String projResp = mockMvc.perform(post("/api/projects")
                .contentType(MediaType.APPLICATION_JSON)
                .content(projectJson))
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();
        Long projectId = objectMapper.readTree(projResp).get("id").asLong();

        mockMvc.perform(put("/api/projects/" + projectId + "/users/" + userId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.users[0].id").value(userId.intValue()));

        mockMvc.perform(put("/api/projects/" + projectId + "/users/" + userId + "/remove"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.users").isEmpty());
    }
}