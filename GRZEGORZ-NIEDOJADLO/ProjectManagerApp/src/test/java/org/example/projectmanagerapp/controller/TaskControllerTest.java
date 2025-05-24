package org.example.projectmanagerapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.projectmanagerapp.entity.project.Project;
import org.example.projectmanagerapp.entity.task.Task;
import org.example.projectmanagerapp.entity.task.TaskType;
import org.example.projectmanagerapp.entity.priority.PriorityLevel;
import org.example.projectmanagerapp.entity.priority.HighPriority;
import org.example.projectmanagerapp.entity.priority.MediumPriority;
import org.example.projectmanagerapp.entity.priority.LowPriority;
import org.example.projectmanagerapp.repository.ProjectRepository;
import org.example.projectmanagerapp.repository.TaskRepository;
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
class TaskControllerIntegrationTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private TaskRepository taskRepository;
    @Autowired private ProjectRepository projectRepository;
    @Autowired private ObjectMapper objectMapper;

    @BeforeEach
    void clearDb() {
        taskRepository.deleteAll();
        projectRepository.deleteAll();
    }

    @Test
    void shouldCreateAndFetchTask() throws Exception {
        Project project = new Project();
        project.setName("Projekt testowy");
        project = projectRepository.save(project);

        Task task = new Task();
        task.setTitle("Zadanie 1");
        task.setDescription("Opis zadania testowego");
        task.setTaskType(TaskType.FEATURE);
        task.setProject(project);


        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(task)))
                .andExpect(status().isOk()) // <-- SPRAWDZENIE, czy POST zadziałał
                .andExpect(jsonPath("$.title").value("Zadanie 1"));

        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Zadanie 1"))
                .andExpect(jsonPath("$[0].description").value("Opis zadania testowego"));
    }
}