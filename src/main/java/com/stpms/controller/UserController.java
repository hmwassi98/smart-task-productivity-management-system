package com.stpms.controller;

import com.stpms.model.User;
import com.stpms.service.UserService;

import java.util.List;
import java.util.Optional;

public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    public User createUser(String username, String email) {
        return userService.createUser(username, email);
    }

    public Optional<User> getUserById(Long userId) {
        return userService.getUserById(userId);
    }

    public Optional<User> getUserByEmail(String email) {
        return userService.getUserByEmail(email);
    }

    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    public User updateUser(User user) {
        return userService.updateUser(user);
    }

    public boolean deleteUser(Long userId) {
        return userService.deleteUser(userId);
    }
}