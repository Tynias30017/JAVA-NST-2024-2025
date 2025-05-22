package org.example.projectmanagerapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.projectmanagerapp.entity.task.Task;
import org.example.projectmanagerapp.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
@Tag(name = "Tasks", description = "Operacje na zadaniach")
public class TaskController {

    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    @Operation(summary = "Pobierz wszystkie zadania")
    public List<Task> getAllTasks() {
        return taskService.getAllTasks();
    }

    @PostMapping
    @Operation(summary = "Utwórz zadanie")
    public Task createTask(
            @Parameter(description = "Dane zadania") @RequestBody Task task) {
        return taskService.createTask(task);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Aktualizuj zadanie po ID")
    public Task updateTask(
            @Parameter(description = "ID zadania") @PathVariable Long id,
            @Parameter(description = "Zaktualizowane dane") @RequestBody Task task) {
        return taskService.updateTask(id, task);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Usuń zadanie po ID")
    public void deleteTask(
            @Parameter(description = "ID zadania") @PathVariable Long id) {
        taskService.deleteTask(id);
    }
}