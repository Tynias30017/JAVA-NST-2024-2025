package org.example.projectmanagerapp.entity.project;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.projectmanagerapp.entity.task.Task;
import org.example.projectmanagerapp.entity.user.User;

import java.util.Set;
import java.util.HashSet;

@Entity (name = "project")
@Getter
@Setter
@NoArgsConstructor
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nazwa projektu nie może być pusta")
    private String name;

    private String description;

    @ManyToMany
    @JoinTable(
            name = "project_users",
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    @JsonIgnoreProperties("projects")
    private Set<User> users = new HashSet<>();


    @OneToMany(mappedBy = "project")
    private Set<Task> tasks;
}