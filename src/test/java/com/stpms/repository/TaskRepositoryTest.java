package com.stpms.repository;

import com.stpms.config.DatabaseConnection;
import com.stpms.model.Task;
import com.stpms.model.TaskCategory;
import com.stpms.model.TaskPriority;
import com.stpms.repository.JdbcTaskRepository;
import com.stpms.repository.TaskRepository;

import java.sql.Connection;
import java.time.LocalDate;

public class TaskRepositoryTest {

    public static void main(String[] args) {
        try (Connection connection = DatabaseConnection.getConnection()) {

            TaskRepository taskRepository = new JdbcTaskRepository(connection);

            Task task = new Task(
                    "Study JDBC",
                    "Practice repository layer implementation",
                    TaskPriority.HIGH,
                    TaskCategory.STUDY,
                    LocalDate.now().plusDays(3)
            );

            task.setUserId(null); // or set an existing user ID if you already have one

            Task savedTask = taskRepository.save(task);

            System.out.println("Saved task:");
            System.out.println(savedTask);
            System.out.println("Generated ID: " + savedTask.getTaskId());

            //findById() test
            System.out.println("\nFinding task by ID...");
            taskRepository.findById(savedTask.getTaskId())
                    .ifPresentOrElse(
                            foundTask -> {
                                System.out.println("Task found:");
                                System.out.println(foundTask);
                            },
                            () -> System.out.println("Task not found.")
                    );

            //findAll() test
            System.out.println("\nAll tasks:");
            taskRepository.findAll().forEach(System.out::println);

            //update() test
            savedTask.setName("Updated Task Name");
            taskRepository.update(savedTask);

            System.out.println("\nAfter update:");
            System.out.println(taskRepository.findById(savedTask.getTaskId()).orElse(null));

            //existsById() test
            System.out.println("\nExists:");
            System.out.println(taskRepository.existsById(savedTask.getTaskId()));

            //deleteById() test
            boolean deleted = taskRepository.deleteById(savedTask.getTaskId());

            System.out.println("\nDeleted: " + deleted);
            System.out.println("Exists after delete: " +
                    taskRepository.existsById(savedTask.getTaskId()));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}