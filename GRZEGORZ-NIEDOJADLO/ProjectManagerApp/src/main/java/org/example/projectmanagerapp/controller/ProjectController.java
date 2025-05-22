package org.example.projectmanagerapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.projectmanagerapp.entity.project.Project;
import org.example.projectmanagerapp.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
@Tag(name = "Projects", description = "Operacje na projektach")
public class ProjectController {

    @Autowired
    private ProjectRepository projectRepository;

    @GetMapping
    @Operation(summary = "Pobierz wszystkie projekty", description = "Zwraca listę wszystkich projektów zapisanych w systemie")
    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    @PostMapping
    @Operation(summary = "Utwórz nowy projekt", description = "Zapisuje nowy projekt do bazy danych")
    public Project createProject(
            @Parameter(description = "Obiekt projektu do zapisania", required = true)
            @RequestBody Project project) {
        return projectRepository.save(project);
    }
}