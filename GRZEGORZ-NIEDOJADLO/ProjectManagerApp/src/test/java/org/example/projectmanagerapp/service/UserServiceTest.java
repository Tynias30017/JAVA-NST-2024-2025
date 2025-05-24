package org.example.projectmanagerapp.service;

import org.example.projectmanagerapp.entity.user.User;
import org.example.projectmanagerapp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    private UserRepository userRepository;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        userService = new UserService(userRepository);
    }

    @Test
    @DisplayName("getAllUsers powinno zwrócić listę użytkowników")
    void testGetAllUsers() {
        User u1 = new User(); u1.setName("A");
        User u2 = new User(); u2.setName("B");

        when(userRepository.findAll()).thenReturn(List.of(u1, u2));

        List<User> users = userService.getAllUsers();

        assertEquals(2, users.size());
        verify(userRepository).findAll();
    }

    @Test
    @DisplayName("createUser powinno zapisać użytkownika")
    void testCreateUser() {
        User user = new User(); user.setName("X");
        when(userRepository.save(user)).thenReturn(user);

        User saved = userService.createUser(user);

        assertEquals("X", saved.getName());
        verify(userRepository).save(user);
    }

    @Test
    @DisplayName("getUserById powinno zwrócić użytkownika jeśli istnieje")
    void testGetUserByIdFound() {
        User user = new User(); user.setName("Jan");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User result = userService.getUserById(1L);

        assertEquals("Jan", result.getName());
    }

    @Test
    @DisplayName("getUserById powinno rzucić wyjątek jeśli nie istnieje")
    void testGetUserByIdNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> userService.getUserById(1L));
    }

    @Test
    @DisplayName("updateUser powinno nadpisać dane")
    void testUpdateUser() {
        User existing = new User(); existing.setId(1L); existing.setName("Old");
        User update = new User(); update.setName("New");

        when(userRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(userRepository.save(any())).thenReturn(existing);

        User result = userService.updateUser(1L, update);

        assertEquals("New", result.getName());
    }

    @Test
    @DisplayName("deleteUser powinno usunąć użytkownika")
    void testDeleteUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(new User()));
        userService.deleteUser(1L);
        verify(userRepository).deleteById(1L);
    }
}