package com.stpms.service;

import com.stpms.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    User createUser(String username, String email);

    Optional<User> getUserById(Long userId);

    Optional<User> getUserByEmail(String email);

    List<User> getAllUsers();

    User updateUser(User user);

    boolean deleteUser(Long userId);
}