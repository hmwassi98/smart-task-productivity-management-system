package com.stpms.service;

import com.stpms.model.User;
import com.stpms.repository.UserRepository;

import java.util.List;
import java.util.Optional;

public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User createUser(String username, String email) {
        User user = new User(username, email);
        return userRepository.save(user);
    }

    @Override
    public Optional<User> getUserById(Long userId) {
        return userRepository.findById(userId);
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User updateUser(User user) {
        if (user.getUserId() == null) {
            throw new IllegalArgumentException("User ID cannot be null when updating.");
        }

        if (!userRepository.existsById(user.getUserId())) {
            throw new IllegalArgumentException("User with ID " + user.getUserId() + " does not exist.");
        }

        return userRepository.update(user);
    }

    @Override
    public boolean deleteUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            return false;
        }

        return userRepository.deleteById(userId);
    }
}