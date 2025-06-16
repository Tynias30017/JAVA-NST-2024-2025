package org.example.projectmanagerapp.service;

import jakarta.persistence.EntityNotFoundException;
import org.example.projectmanagerapp.entity.project.Project;
import org.example.projectmanagerapp.entity.user.User;
import org.example.projectmanagerapp.repository.ProjectRepository;
import org.springframework.stereotype.Service;
import org.example.projectmanagerapp.repository.UserRepository;

import java.util.List;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    public ProjectService(ProjectRepository projectRepository, UserRepository userRepository) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    public Project createProject(Project project) {
        if (project.getDescription() == null) {
            project.setDescription("desc");
        }
        return projectRepository.save(project);
    }

    public Project getProjectById(Long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Projekt o ID " + id + " nie istnieje."));
    }

    public Project updateProject(Long id, Project updatedProject) {
        Project project = getProjectById(id);
        project.setName(updatedProject.getName());
        project.setUsers(updatedProject.getUsers());
        project.setDescription(updatedProject.getDescription());
        return projectRepository.save(project);
    }

    public void deleteProject(Long projectId) {
        if (!projectRepository.existsById(projectId)) {
            throw new EntityNotFoundException("Project not found with id: " + projectId);
        }
        projectRepository.deleteById(projectId);
    }

    public Project assignUserToProject(Long projectId, Long userId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("Project not found with id: " + projectId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));

        project.getUsers().add(user);

        return projectRepository.save(project);
    }

    public Project removeUserFromProject(Long projectId, Long userId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("Project not found with id: " + projectId));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));
        if (!project.getUsers().contains(user)) {
            return project;
        }
        project.getUsers().remove(user);
        return projectRepository.save(project);
    }

    public Project assignUsersToProject(Long projectId, List<Long> userIds) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("Project not found with id: " + projectId));
        List<User> users = userRepository.findAllById(userIds);
        project.getUsers().addAll(users);
        return projectRepository.save(project);
    }
}