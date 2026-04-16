package com.stpms.service;

import com.stpms.config.DatabaseConnection;
import com.stpms.model.Task;
import com.stpms.model.TaskCategory;
import com.stpms.model.TaskPriority;
import com.stpms.model.TaskStatus;
import com.stpms.repository.JdbcTaskRepository;
import com.stpms.repository.TaskRepository;

import java.sql.Connection;
import java.time.LocalDate;

public class TaskServiceTest {

    public static void main(String[] args) {
        try (Connection connection = DatabaseConnection.getConnection()) {

            TaskRepository taskRepository = new JdbcTaskRepository(connection);
            TaskService taskService = new TaskServiceImpl(taskRepository);

            Task createdTask = taskService.createTask(
                    "Service Layer Task",
                    "Testing task service methods",
                    TaskPriority.HIGH,
                    TaskCategory.STUDY,
                    LocalDate.now().plusDays(5)
            );

            System.out.println("Created task:");
            System.out.println(createdTask);
            System.out.println("Generated ID: " + createdTask.getTaskId());

            System.out.println("\nFinding task by ID...");
            taskService.getTaskById(createdTask.getTaskId())
                    .ifPresentOrElse(
                            foundTask -> {
                                System.out.println("Task found:");
                                System.out.println(foundTask);
                            },
                            () -> System.out.println("Task not found.")
                    );

            System.out.println("\nAll tasks:");
            taskService.getAllTasks().forEach(System.out::println);

            System.out.println("\nTasks by status (TODO):");
            taskService.getTasksByStatus(TaskStatus.TODO).forEach(System.out::println);

            System.out.println("\nStarting task...");
            Task startedTask = taskService.startTask(createdTask.getTaskId());
            System.out.println(startedTask);

            System.out.println("\nCompleting task...");
            Task completedTask = taskService.completeTask(createdTask.getTaskId());
            System.out.println(completedTask);

            System.out.println("\nReopening task...");
            Task reopenedTask = taskService.reopenTask(createdTask.getTaskId());
            System.out.println(reopenedTask);

            reopenedTask.setName("Updated Service Task");
            reopenedTask.setDescription("Updated through TaskService");
            reopenedTask.setPriority(TaskPriority.MEDIUM);
            reopenedTask.setCategory(TaskCategory.WORK);

            Task updatedTask = taskService.updateTask(reopenedTask);

            System.out.println("\nUpdated task:");
            System.out.println(updatedTask);

            boolean deleted = taskService.deleteTask(createdTask.getTaskId());
            System.out.println("\nDeleted: " + deleted);

            System.out.println("Finding task after delete...");
            taskService.getTaskById(createdTask.getTaskId())
                    .ifPresentOrElse(
                            task -> System.out.println("Task still exists: " + task),
                            () -> System.out.println("Task no longer exists.")
                    );

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}