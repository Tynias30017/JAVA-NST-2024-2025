package com.example.service;


import com.example.repository.ProjectRepository;
import com.example.entity.Project;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public List<Project> findAll() {
        return projectRepository.findAll();
    }

    public Optional<Project> findById(Long id) {
        return projectRepository.findById(id);
    }

    public Project save(Project project) {
        return projectRepository.save(project);
    }

    public Project update(Long id, Project updatedProject) {
        return projectRepository.findById(id).map(project -> {
            project.setName(updatedProject.getName());
            project.setDescription(updatedProject.getDescription());
            return projectRepository.save(project);
        }).orElseThrow(() -> new RuntimeException("Project not found with id " + id));
    }

    public void delete(Long id) {
        projectRepository.deleteById(id);
    }

    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }
}
