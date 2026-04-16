package com.stpms.service;

import com.stpms.config.DatabaseConnection;
import com.stpms.model.Subtask;
import com.stpms.model.Task;
import com.stpms.model.TaskCategory;
import com.stpms.model.TaskPriority;
import com.stpms.repository.JdbcSubtaskRepository;
import com.stpms.repository.JdbcTaskRepository;
import com.stpms.repository.SubtaskRepository;
import com.stpms.repository.TaskRepository;

import java.sql.Connection;
import java.time.LocalDate;

public class SubtaskServiceTest {

    public static void main(String[] args) {
        try (Connection connection = DatabaseConnection.getConnection()) {

            TaskRepository taskRepository = new JdbcTaskRepository(connection);
            SubtaskRepository subtaskRepository = new JdbcSubtaskRepository(connection);

            TaskService taskService = new TaskServiceImpl(taskRepository);
            SubtaskService subtaskService = new SubtaskServiceImpl(subtaskRepository, taskRepository);

            Task parentTask = taskService.createTask(
                    "Parent Task for Subtask Service Test",
                    "Testing subtask service methods",
                    TaskPriority.HIGH,
                    TaskCategory.WORK,
                    LocalDate.now().plusDays(3)
            );

            System.out.println("Created parent task:");
            System.out.println(parentTask);

            Subtask createdSubtask = subtaskService.createSubtask(
                    parentTask.getTaskId(),
                    "Write subtask service test"
            );

            System.out.println("\nCreated subtask:");
            System.out.println(createdSubtask);
            System.out.println("Generated ID: " + createdSubtask.getSubtaskId());

            System.out.println("\nFinding subtask by ID...");
            subtaskService.getSubtaskById(createdSubtask.getSubtaskId())
                    .ifPresentOrElse(
                            foundSubtask -> {
                                System.out.println("Subtask found:");
                                System.out.println(foundSubtask);
                            },
                            () -> System.out.println("Subtask not found.")
                    );

            System.out.println("\nAll subtasks:");
            subtaskService.getAllSubtasks().forEach(System.out::println);

            System.out.println("\nSubtasks by task ID:");
            subtaskService.getSubtasksByTaskId(parentTask.getTaskId()).forEach(System.out::println);

            System.out.println("\nCompleting subtask...");
            Subtask completedSubtask = subtaskService.completeSubtask(createdSubtask.getSubtaskId());
            System.out.println(completedSubtask);

            System.out.println("\nReopening subtask...");
            Subtask reopenedSubtask = subtaskService.reopenSubtask(createdSubtask.getSubtaskId());
            System.out.println(reopenedSubtask);

            reopenedSubtask.setName("Updated subtask through service");
            Subtask updatedSubtask = subtaskService.updateSubtask(reopenedSubtask);

            System.out.println("\nUpdated subtask:");
            System.out.println(updatedSubtask);

            boolean deleted = subtaskService.deleteSubtask(createdSubtask.getSubtaskId());
            System.out.println("\nDeleted subtask: " + deleted);

            System.out.println("Finding subtask after delete...");
            subtaskService.getSubtaskById(createdSubtask.getSubtaskId())
                    .ifPresentOrElse(
                            subtask -> System.out.println("Subtask still exists: " + subtask),
                            () -> System.out.println("Subtask no longer exists.")
                    );

            taskService.deleteTask(parentTask.getTaskId());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}