package com.stpms.repository;

import com.stpms.config.DatabaseConnection;
import com.stpms.model.Subtask;
import com.stpms.model.Task;
import com.stpms.model.TaskCategory;
import com.stpms.model.TaskPriority;

import java.sql.Connection;
import java.time.LocalDate;

public class SubtaskRepositoryTest {

    public static void main(String[] args) {
        try (Connection connection = DatabaseConnection.getConnection()) {

            TaskRepository taskRepository = new JdbcTaskRepository(connection);
            SubtaskRepository subtaskRepository = new JdbcSubtaskRepository(connection);

            Task parentTask = new Task(
                    "Parent Task for Subtask Test",
                    "Used to test subtasks",
                    TaskPriority.HIGH,
                    TaskCategory.WORK,
                    LocalDate.now().plusDays(3)
            );

            Task savedTask = taskRepository.save(parentTask);

            System.out.println("Saved parent task:");
            System.out.println(savedTask);
            System.out.println("Parent Task ID: " + savedTask.getTaskId());

            Subtask subtask = new Subtask("Write repository test");
            subtask.setParentTaskId(savedTask.getTaskId());

            Subtask savedSubtask = subtaskRepository.save(subtask);

            System.out.println("\nSaved subtask:");
            System.out.println(savedSubtask);
            System.out.println("Generated Subtask ID: " + savedSubtask.getSubtaskId());

            System.out.println("\nFinding subtask by ID...");
            subtaskRepository.findById(savedSubtask.getSubtaskId())
                    .ifPresentOrElse(
                            foundSubtask -> {
                                System.out.println("Subtask found:");
                                System.out.println(foundSubtask);
                            },
                            () -> System.out.println("Subtask not found.")
                    );

            System.out.println("\nAll subtasks:");
            subtaskRepository.findAll().forEach(System.out::println);

            System.out.println("\nSubtasks by task ID:");
            subtaskRepository.findByTaskId(savedTask.getTaskId()).forEach(System.out::println);

            savedSubtask.setName("Write and verify repository test");
            savedSubtask.setCompleted(true);
            Subtask updatedSubtask = subtaskRepository.update(savedSubtask);

            System.out.println("\nUpdated subtask:");
            System.out.println(updatedSubtask);

            System.out.println("\nExists by ID:");
            System.out.println(subtaskRepository.existsById(savedSubtask.getSubtaskId()));

            boolean deleted = subtaskRepository.deleteById(savedSubtask.getSubtaskId());
            System.out.println("\nDeleted: " + deleted);

            System.out.println("Exists after delete:");
            System.out.println(subtaskRepository.existsById(savedSubtask.getSubtaskId()));

            taskRepository.deleteById(savedTask.getTaskId());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}