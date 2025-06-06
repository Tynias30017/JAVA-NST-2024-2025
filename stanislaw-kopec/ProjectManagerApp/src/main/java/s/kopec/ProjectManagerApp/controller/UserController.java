package s.kopec.ProjectManagerApp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import s.kopec.ProjectManagerApp.entity.User;
import s.kopec.ProjectManagerApp.repository.UserRepository;
import s.kopec.ProjectManagerApp.service.UserService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Users", description = "Operations for mapping users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("")
    @Operation(summary = "Retrieve all users", description = "Returns a list of all users from the database")
    public List<User> getAll() {
        return userService.getAllUsers();
    }



    @GetMapping("/{id}")
    @Operation(summary = "Retrieve user by ID", description = "Returns a user by their ID")
    public Optional<User> getById(
            @Parameter(description = "ID of the user to retrieve", required = true)
            @PathVariable Long id) {
        return userService.findUserById(id);
    }

    @PostMapping("/create")
    @Operation(summary = "Create a new user", description = "Adds a new user to the database")
    public User create(
            @Parameter(description = "User object to create", required = true)
            @RequestBody User user) {
        return userService.createUser(user);
    }


    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Delete a user by ID", description = "Deletes a user from the database using their ID")
    public void deleteById(
            @Parameter(description = "ID of the user to delete", required = true)
            @PathVariable Long id) {
        userService.deleteUserById(id);
    }

    @PutMapping("/update/{id}/{newUserName}")
    @Operation(summary = "Update user's username", description = "Updates the username of an existing user")
    public void update(
            @Parameter(description = "ID of the user to update", required = true)
            @PathVariable Long id,
            @Parameter(description = "New username", required = true)
            @PathVariable String newUserName) {
        userService.updateUserName(id, newUserName);
    }
}
