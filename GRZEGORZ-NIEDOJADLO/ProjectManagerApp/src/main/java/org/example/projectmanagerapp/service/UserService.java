package org.example.projectmanagerapp.service;

import org.example.projectmanagerapp.entity.user.User;
import org.example.projectmanagerapp.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public User updateUser(Long id, User updatedUser) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setName(updatedUser.getName());
            return userRepository.save(user);
        } else {
            throw new RuntimeException("Użytkownik o ID " + id + " nie istnieje.");
        }
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}