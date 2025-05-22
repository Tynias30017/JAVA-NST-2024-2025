package org.example.projectmanagerapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.projectmanagerapp.entity.task.Task;
import org.example.projectmanagerapp.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
@Tag(name = "Tasks", description = "Operacje na zadaniach (Tasks)")
public class TaskController {

    private final TaskRepository taskRepository;

    @Autowired
    public TaskController(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @GetMapping
    @Operation(summary = "Pobierz wszystkie zadania", description = "Zwraca listę wszystkich zadań zapisanych w bazie")
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    @PostMapping
    @Operation(summary = "Utwórz nowe zadanie", description = "Tworzy nowe zadanie na podstawie danych wejściowych")
    public Task createTask(
            @Parameter(description = "Obiekt zadania do zapisania", required = true)
            @RequestBody Task task) {
        return taskRepository.save(task);
    }
}