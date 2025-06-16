package org.example.projectmanagerapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.projectmanagerapp.entity.project.Project;
import org.example.projectmanagerapp.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import jakarta.validation.Valid;
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
            @Parameter(description = "Dane projektu") @Valid @RequestBody Project project) {
        return projectService.createProject(project);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Aktualizuj projekt po ID")
    public Project updateProject(
            @Parameter(description = "ID projektu") @PathVariable Long id,
            @Parameter(description = "Zaktualizowany projekt") @Valid @RequestBody Project project) {
        return projectService.updateProject(id, project);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Usuń projekt po ID")
    public ResponseEntity<Void> deleteProjectById(@PathVariable Long id) {
        projectService.deleteProject(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Pobierz projekt po ID")
    public ResponseEntity<Project> getProjectById(@PathVariable Long id) {
        Project project = projectService.getProjectById(id);
        if (project != null) {
            return ResponseEntity.ok(project);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{projectId}/users/{userId}")
    @Operation(summary = "Przypisz użytkownika do projektu")
    public ResponseEntity<Project> assignUserToProject(
            @PathVariable Long projectId,
            @PathVariable Long userId) {
        Project updatedProject = projectService.assignUserToProject(projectId, userId);
        return ResponseEntity.ok(updatedProject);
    }

    @PutMapping("/{projectId}/users/{userId}/remove")
    @Operation(summary = "Usuń użytkownika z projektu")
    public ResponseEntity<Project> removeUserFromProject(
            @PathVariable Long projectId,
            @PathVariable Long userId) {
        Project updatedProject = projectService.removeUserFromProject(projectId, userId);
        return ResponseEntity.ok(updatedProject);
    }

    @PostMapping("/{projectId}/users")
    @Operation(summary = "Przypisz wielu użytkowników do projektu")
    public ResponseEntity<Project> assignUsersToProject(
            @PathVariable Long projectId,
            @RequestBody List<Long> userIds) {
        Project updatedProject = projectService.assignUsersToProject(projectId, userIds);
        return ResponseEntity.ok(updatedProject);
    }
}