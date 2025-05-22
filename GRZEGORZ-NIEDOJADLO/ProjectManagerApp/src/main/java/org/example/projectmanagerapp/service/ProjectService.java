package org.example.projectmanagerapp.service;

import org.example.projectmanagerapp.entity.project.Project;
import org.example.projectmanagerapp.repository.ProjectRepository;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public Project getProjectById(Long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Projekt o ID " + id + " nie istnieje."));
    }

    public Project updateProject(Long id, Project updatedProject) {
        Project project = getProjectById(id);
        project.setName(updatedProject.getName());
        project.setUsers(updatedProject.getUsers());
        return projectRepository.save(project);
    }

    public void deleteProject(Long id) {
        getProjectById(id);
        projectRepository.deleteById(id);
    }
}