package org.example.projectmanagerapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.projectmanagerapp.entity.user.User;
import org.example.projectmanagerapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Users", description = "Operacje na użytkownikach")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    @Operation(summary = "Pobierz wszystkich użytkowników", description = "Zwraca listę wszystkich użytkowników w systemie")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @PostMapping
    @Operation(summary = "Utwórz użytkownika", description = "Zapisuje nowego użytkownika w bazie danych")
    public User createUser(
            @Parameter(description = "Dane nowego użytkownika", required = true)
            @RequestBody User user) {
        return userRepository.save(user);
    }
}