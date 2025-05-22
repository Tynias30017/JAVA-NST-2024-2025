package org.example.projectmanagerapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.projectmanagerapp.entity.project.Project;
import org.example.projectmanagerapp.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
@Tag(name = "Projects", description = "Operacje na projektach")
public class ProjectController {

    private final ProjectService projectService;

    @Autowired
    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping
    @Operation(summary = "Pobierz wszystkie projekty")
    public List<Project> getAllProjects() {
        return projectService.getAllProjects();
    }

    @PostMapping
    @Operation(summary = "Utwórz projekt")
    public Project createProject(
            @Parameter(description = "Dane projektu") @RequestBody Project project) {
        return projectService.createProject(project);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Aktualizuj projekt po ID")
    public Project updateProject(
            @Parameter(description = "ID projektu") @PathVariable Long id,
            @Parameter(description = "Zaktualizowany projekt") @RequestBody Project project) {
        return projectService.updateProject(id, project);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Usuń projekt po ID")
    public void deleteProject(
            @Parameter(description = "ID projektu") @PathVariable Long id) {
        projectService.deleteProject(id);
    }
}