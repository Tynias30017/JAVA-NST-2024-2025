package org.example.projectmanagerapp.repository;

import org.example.projectmanagerapp.IntegrationTestBase;
import org.example.projectmanagerapp.entity.project.Project;
import org.example.projectmanagerapp.entity.task.Task;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

class RepositoryIntegrationTest extends IntegrationTestBase {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Test
    void testSaveAndFindProject() {
        Project project = new Project();
        project.setName("TestProject");
        Project savedProject = projectRepository.save(project);

        assertThat(savedProject.getName()).isEqualTo("TestProject");
        assertThat(projectRepository.findById(savedProject.getId())).isPresent();
    }

    @Test
    void testSaveTaskWithProject() {
        Project project = new Project();
        project.setName("ParentProject");
        Project savedProject = projectRepository.save(project);

        Task task = new Task();
        task.setTitle("TestTask");
        task.setDescription("Opis zadania testowego");
        task.setTaskType(org.example.projectmanagerapp.entity.task.TaskType.FEATURE);
        task.setProject(savedProject);
        Task savedTask = taskRepository.save(task);

        assertThat(savedTask.getProject()).isEqualTo(savedProject);
        assertThat(taskRepository.findById(savedTask.getId()).get().getTitle()).isEqualTo("TestTask");
    }
}