package org.example.projectmanager.controller;

import org.example.projectmanager.entity.Project;
//import org.example.projectmanager.repository.ProjectRepository;
import org.example.projectmanager.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

@RestController
@RequestMapping("/api/projects")
@Tag (name = "Project",description = "Operations related to project management")

public class ProjectController {
    @Autowired
    //private ProjectRepository projectRepository;
    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping
    @Operation (summary = "Get all projects",description = "Returns a list of all projects")
    public List<Project> getAllProjects() {
        return projectService.getAllProjects();
    }

    @PostMapping
    @Operation(summary = "Create a new project",description = "Creates a new project and saves it to the database")
    public ResponseEntity<Project> addProject(
            @Parameter(description = "Project object that needs to be created", required = true)
            @RequestBody Project project) {
        Project createdProject = projectService.createProject(project);
        return new ResponseEntity<>(createdProject, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a project", description = "Updates an existing project by ID")
    public ResponseEntity<Project> updateProject(
            @Parameter(description = "ID of the project to be updated", required = true)
            @PathVariable Long id,
            @RequestBody Project project) {
        Project updatedProject = projectService.updateProject(id, project);
        return ResponseEntity.ok(updatedProject);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a project", description = "Deletes a project by ID")
    public ResponseEntity<Void> deleteProject(
            @Parameter(description = "ID of the project to be deleted", required = true)
            @PathVariable Long id) {
        projectService.deleteProject(id);
        return ResponseEntity.noContent().build();
    }
}