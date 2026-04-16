package com.stpms.repository;

import com.stpms.config.DatabaseConnection;
import com.stpms.model.User;

import java.sql.Connection;

public class UserRepositoryTest {

    public static void main(String[] args) {
        try (Connection connection = DatabaseConnection.getConnection()) {

            UserRepository userRepository = new JdbcUserRepository(connection);

            User user = new User("testuser01", "testuser01@example.com");

            User savedUser = userRepository.save(user);

            System.out.println("Saved user:");
            System.out.println(savedUser);
            System.out.println("Generated ID: " + savedUser.getUserId());

            System.out.println("\nFinding user by ID...");
            userRepository.findById(savedUser.getUserId())
                    .ifPresentOrElse(
                            foundUser -> {
                                System.out.println("User found:");
                                System.out.println(foundUser);
                            },
                            () -> System.out.println("User not found.")
                    );

            System.out.println("\nFinding user by email...");
            userRepository.findByEmail(savedUser.getEmail())
                    .ifPresentOrElse(
                            foundUser -> {
                                System.out.println("User found by email:");
                                System.out.println(foundUser);
                            },
                            () -> System.out.println("User not found by email.")
                    );

            System.out.println("\nAll users:");
            userRepository.findAll().forEach(System.out::println);

            savedUser.setUsername("updated_testuser01");
            savedUser.setEmail("updated_testuser01@example.com");
            User updatedUser = userRepository.update(savedUser);

            System.out.println("\nUpdated user:");
            System.out.println(updatedUser);

            System.out.println("\nExists by ID:");
            System.out.println(userRepository.existsById(savedUser.getUserId()));

            boolean deleted = userRepository.deleteById(savedUser.getUserId());
            System.out.println("\nDeleted: " + deleted);

            System.out.println("Exists after delete:");
            System.out.println(userRepository.existsById(savedUser.getUserId()));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}