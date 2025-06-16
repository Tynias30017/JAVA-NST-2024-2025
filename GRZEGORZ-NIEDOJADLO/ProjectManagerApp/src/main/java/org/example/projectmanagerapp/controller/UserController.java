package org.example.projectmanagerapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.projectmanagerapp.entity.user.User;
import org.example.projectmanagerapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Users", description = "Operacje na użytkownikach")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @Operation(summary = "Pobierz wszystkich użytkowników")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping
    @Operation(summary = "Utwórz użytkownika")
    public User createUser(
            @Parameter(description = "Dane nowego użytkownika", required = true)
            @Valid @RequestBody User user) {
        return userService.createUser(user);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Aktualizuj użytkownika po ID")
    public User updateUser(
            @Parameter(description = "ID użytkownika") @PathVariable Long id,
            @Parameter(description = "Zaktualizowane dane") @Valid @RequestBody User user) {
        return userService.updateUser(id, user);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Usuń użytkownika po ID")
    public void deleteUser(
            @Parameter(description = "ID użytkownika") @PathVariable Long id) {
        userService.deleteUser(id);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Pobierz użytkownika po ID")
    public User getUserById(
            @Parameter(description = "ID użytkownika", required = true)
            @PathVariable Long id) {
        return userService.getUserById(id);
    }
}
