package com.stpms.service;

import com.stpms.config.DatabaseConnection;
import com.stpms.model.User;
import com.stpms.repository.JdbcUserRepository;
import com.stpms.repository.UserRepository;

import java.sql.Connection;

public class UserServiceTest {

    public static void main(String[] args) {
        try (Connection connection = DatabaseConnection.getConnection()) {

            UserRepository userRepository = new JdbcUserRepository(connection);
            UserService userService = new UserServiceImpl(userRepository);

            User createdUser = userService.createUser(
                    "service_user_01",
                    "service_user_01@example.com"
            );

            System.out.println("Created user:");
            System.out.println(createdUser);
            System.out.println("Generated ID: " + createdUser.getUserId());

            System.out.println("\nFinding user by ID...");
            userService.getUserById(createdUser.getUserId())
                    .ifPresentOrElse(
                            foundUser -> {
                                System.out.println("User found:");
                                System.out.println(foundUser);
                            },
                            () -> System.out.println("User not found.")
                    );

            System.out.println("\nFinding user by email...");
            userService.getUserByEmail(createdUser.getEmail())
                    .ifPresentOrElse(
                            foundUser -> {
                                System.out.println("User found by email:");
                                System.out.println(foundUser);
                            },
                            () -> System.out.println("User not found by email.")
                    );

            System.out.println("\nAll users:");
            userService.getAllUsers().forEach(System.out::println);

            createdUser.setUsername("updated_service_user_01");
            createdUser.setEmail("updated_service_user_01@example.com");

            User updatedUser = userService.updateUser(createdUser);

            System.out.println("\nUpdated user:");
            System.out.println(updatedUser);

            boolean deleted = userService.deleteUser(createdUser.getUserId());
            System.out.println("\nDeleted: " + deleted);

            System.out.println("Finding user after delete...");
            userService.getUserById(createdUser.getUserId())
                    .ifPresentOrElse(
                            user -> System.out.println("User still exists: " + user),
                            () -> System.out.println("User no longer exists.")
                    );

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}