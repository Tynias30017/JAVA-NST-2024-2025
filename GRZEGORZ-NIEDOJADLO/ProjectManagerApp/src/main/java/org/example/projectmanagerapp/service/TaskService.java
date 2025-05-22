package org.example.projectmanagerapp.service;

import org.example.projectmanagerapp.entity.task.Task;
import org.example.projectmanagerapp.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    public Task updateTask(Long id, Task updatedTask) {
        Optional<Task> optionalTask = taskRepository.findById(id);
        if (optionalTask.isPresent()) {
            Task task = optionalTask.get();
            task.setTitle(updatedTask.getTitle());
            task.setDescription(updatedTask.getDescription());
            task.setTaskType(updatedTask.getTaskType());
            task.setProject(updatedTask.getProject());
            task.setPriorityLevel(updatedTask.getPriorityLevel());
            return taskRepository.save(task);
        } else {
            throw new RuntimeException("Zadanie o ID " + id + " nie istnieje.");
        }
    }

    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }
}