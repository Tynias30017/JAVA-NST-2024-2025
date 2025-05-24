package org.example.projectmanagerapp.service;

import org.example.projectmanagerapp.entity.project.Project;
import org.example.projectmanagerapp.repository.ProjectRepository;
import org.junit.jupiter.api.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProjectServiceTest {

    private ProjectRepository projectRepository;
    private ProjectService projectService;

    @BeforeEach
    void setUp() {
        projectRepository = mock(ProjectRepository.class);
        projectService = new ProjectService(projectRepository);
    }

    @Test
    void getAllProjects() {
        when(projectRepository.findAll()).thenReturn(List.of(new Project(), new Project()));
        assertEquals(2, projectService.getAllProjects().size());
        verify(projectRepository).findAll();
    }

    @Test
    void createProject() {
        Project p = new Project(); p.setName("Nowy");
        when(projectRepository.save(p)).thenReturn(p);
        assertEquals("Nowy", projectService.createProject(p).getName());
    }

    @Test
    void getProjectById_found() {
        Project p = new Project(); p.setName("X");
        when(projectRepository.findById(1L)).thenReturn(Optional.of(p));
        assertEquals("X", projectService.getProjectById(1L).getName());
    }

    @Test
    void getProjectById_notFound() {
        when(projectRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> projectService.getProjectById(1L));
    }

    @Test
    void updateProject() {
        Project old = new Project(); old.setName("Old");
        Project updated = new Project(); updated.setName("New");

        when(projectRepository.findById(1L)).thenReturn(Optional.of(old));
        when(projectRepository.save(any())).thenReturn(old);

        assertEquals("New", projectService.updateProject(1L, updated).getName());
    }

    @Test
    void deleteProject() {
        when(projectRepository.findById(1L)).thenReturn(Optional.of(new Project()));
        projectService.deleteProject(1L);
        verify(projectRepository).deleteById(1L);
    }
}