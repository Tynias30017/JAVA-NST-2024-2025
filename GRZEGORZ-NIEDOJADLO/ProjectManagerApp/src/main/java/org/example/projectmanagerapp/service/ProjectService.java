package org.example.projectmanagerapp.service;

import org.example.projectmanagerapp.entity.project.Project;
import org.example.projectmanagerapp.repository.ProjectRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    public Project createProject(Project project) {
        return projectRepository.save(project);
    }

    public Project updateProject(Long id, Project updatedProject) {
        Optional<Project> optionalProject = projectRepository.findById(id);
        if (optionalProject.isPresent()) {
            Project project = optionalProject.get();
            project.setName(updatedProject.getName());
            project.setUsers(updatedProject.getUsers());
            return projectRepository.save(project);
        } else {
            throw new RuntimeException("Projekt o ID " + id + " nie istnieje.");
        }
    }

    public void deleteProject(Long id) {
        projectRepository.deleteById(id);
    }
}