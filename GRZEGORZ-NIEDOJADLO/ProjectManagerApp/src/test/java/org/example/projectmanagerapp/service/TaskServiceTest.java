package org.example.projectmanagerapp.service;

import org.example.projectmanagerapp.entity.task.Task;
import org.example.projectmanagerapp.entity.task.TaskType;
import org.example.projectmanagerapp.repository.TaskRepository;
import org.junit.jupiter.api.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaskServiceTest {

    private TaskRepository taskRepository;
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        taskRepository = mock(TaskRepository.class);
        taskService = new TaskService(taskRepository);
    }

    @Test
    void getAllTasks() {
        when(taskRepository.findAll()).thenReturn(List.of(new Task(), new Task()));
        assertEquals(2, taskService.getAllTasks().size());
    }

    @Test
    void createTask() {
        Task t = new Task(); t.setTitle("Test");
        when(taskRepository.save(t)).thenReturn(t);
        assertEquals("Test", taskService.createTask(t).getTitle());
    }

    @Test
    void getTaskById_found() {
        Task t = new Task(); t.setTitle("X");
        when(taskRepository.findById(1L)).thenReturn(Optional.of(t));
        assertEquals("X", taskService.getTaskById(1L).getTitle());
    }

    @Test
    void getTaskById_notFound() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> taskService.getTaskById(1L));
    }

    @Test
    void updateTask() {
        Task old = new Task(); old.setTitle("Old");
        Task updated = new Task(); updated.setTitle("New");
        updated.setTaskType(TaskType.FEATURE);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(old));
        when(taskRepository.save(any())).thenReturn(old);

        assertEquals("New", taskService.updateTask(1L, updated).getTitle());
    }

    @Test
    void deleteTask() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(new Task()));
        taskService.deleteTask(1L);
        verify(taskRepository).deleteById(1L);
    }
}