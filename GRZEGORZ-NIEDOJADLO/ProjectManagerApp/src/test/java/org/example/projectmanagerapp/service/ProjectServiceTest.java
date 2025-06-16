package org.example.projectmanagerapp.service;

import org.example.projectmanagerapp.entity.project.Project;
import org.example.projectmanagerapp.repository.ProjectRepository;
import org.junit.jupiter.api.*;
import org.example.projectmanagerapp.repository.UserRepository;
import jakarta.persistence.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProjectServiceTest {

    private ProjectRepository projectRepository;
    private UserRepository userRepository;
    private ProjectService projectService;

    @BeforeEach
    void setUp() {
        projectRepository = mock(ProjectRepository.class);
        userRepository = mock(UserRepository.class);
        projectService = new ProjectService(projectRepository, userRepository);
    }

    @Test
    void getAllProjects() {
        when(projectRepository.findAll()).thenReturn(List.of(new Project(), new Project()));
        assertEquals(2, projectService.getAllProjects().size());
        verify(projectRepository).findAll();
    }

    @Test
    void createProject() {
        Project p = new Project();
        p.setName("Nowy");
        when(projectRepository.save(p)).thenReturn(p);
        assertEquals("Nowy", projectService.createProject(p).getName());
    }

    @Test
    void getProjectById_found() {
        Project p = new Project();
        p.setName("X");
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
        Project old = new Project();
        old.setName("Old");
        Project updated = new Project();
        updated.setName("New");

        when(projectRepository.findById(1L)).thenReturn(Optional.of(old));
        when(projectRepository.save(any())).thenReturn(old);

        assertEquals("New", projectService.updateProject(1L, updated).getName());
    }

    @Test
    void deleteProject_found() {
        when(projectRepository.existsById(1L)).thenReturn(true);
        projectService.deleteProject(1L);
        verify(projectRepository).deleteById(1L);
    }

    @Test
    void deleteProject_notFound() {
        when(projectRepository.existsById(1L)).thenReturn(false);
        assertThrows(jakarta.persistence.EntityNotFoundException.class, () -> projectService.deleteProject(1L));
    }
}