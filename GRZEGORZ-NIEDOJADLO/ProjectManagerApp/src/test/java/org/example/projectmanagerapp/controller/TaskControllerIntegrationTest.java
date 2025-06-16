package org.example.projectmanagerapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.projectmanagerapp.entity.task.Task;
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
class TaskControllerIntegrationTest {
    @Autowired
    MockMvc mvc;
    @Autowired
    ObjectMapper mapper;

    @Test
    void shouldReturn404WhenTaskNotFound() throws Exception {
        mvc.perform(get("/tasks/999999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturn404WhenDeleteTaskNotFound() throws Exception {
        mvc.perform(delete("/tasks/999999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturn404WhenUpdateTaskNotFound() throws Exception {
        org.example.projectmanagerapp.entity.project.Project project = new org.example.projectmanagerapp.entity.project.Project();
        project.setName("proj");
        project.setDescription("desc");
        String projectJson = mapper.writeValueAsString(project);
        String projResp = mvc.perform(post("/api/projects")
                .contentType(MediaType.APPLICATION_JSON)
                .content(projectJson))
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();
        Long projectId = mapper.readValue(projResp, org.example.projectmanagerapp.entity.project.Project.class).getId();

        Task task = new Task();
        task.setTitle("test");
        task.setDescription("desc");
        task.setTaskType(org.example.projectmanagerapp.entity.task.TaskType.FEATURE); // lub inny istniejący enum
        org.example.projectmanagerapp.entity.project.Project p = new org.example.projectmanagerapp.entity.project.Project();
        p.setId(projectId);
        task.setProject(p);
        String json = mapper.writeValueAsString(task);
        mvc.perform(put("/tasks/999999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturn400WhenCreateTaskWithInvalidData() throws Exception {
        Task task = new Task(); // brak wymaganych pól
        String json = mapper.writeValueAsString(task);
        mvc.perform(post("/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest());
    }
}
