package org.example.projectmanagerapp.service;

import org.example.projectmanagerapp.entity.task.Task;
import org.example.projectmanagerapp.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

    public Task getTaskById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Zadanie o ID " + id + " nie istnieje."));
    }

    public Task updateTask(Long id, Task updatedTask) {
        Task task = getTaskById(id);
        task.setTitle(updatedTask.getTitle());
        task.setDescription(updatedTask.getDescription());
        task.setTaskType(updatedTask.getTaskType());
        task.setProject(updatedTask.getProject());
        task.setPriorityLevel(updatedTask.getPriorityLevel());
        return taskRepository.save(task);
    }

    public void deleteTask(Long id) {
        getTaskById(id);
        taskRepository.deleteById(id);
    }
}