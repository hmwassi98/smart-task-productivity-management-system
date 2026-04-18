package com.stpms.ui;

import com.stpms.Application;
import com.stpms.controller.TaskController;
import com.stpms.model.Task;
import com.stpms.model.TaskCategory;
import com.stpms.model.TaskPriority;
import com.stpms.model.TaskStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class ConsoleUI {

    private final TaskController taskController;
    private final Scanner scanner;

    public ConsoleUI(Application app) {
        this.taskController = app.getTaskController();
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        boolean running = true;

        while (running) {
            printMainMenu();
            int choice = readInt("Choose an option: ");

            switch (choice) {
                case 1 -> taskMenu();
                case 2 -> System.out.println("User management UI not implemented yet.");
                case 3 -> System.out.println("Subtask management UI not implemented yet.");
                case 4 -> System.out.println("Pomodoro session UI not implemented yet.");
                case 5 -> {
                    running = false;
                    System.out.println("Exiting STPMS...");
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }

        scanner.close();
    }

    private void printMainMenu() {
        System.out.println("\n=== STPMS Main Menu ===");
        System.out.println("1. Manage Tasks");
        System.out.println("2. Manage Users");
        System.out.println("3. Manage Subtasks");
        System.out.println("4. Manage Pomodoro Sessions");
        System.out.println("5. Exit");
    }

    private void taskMenu() {
        boolean inTaskMenu = true;

        while (inTaskMenu) {
            printTaskMenu();
            int choice = readInt("Choose a task option: ");

            switch (choice) {
                case 1 -> createTask();
                case 2 -> viewTaskById();
                case 3 -> viewAllTasks();
                case 4 -> viewTasksByStatus();
                case 5 -> startTask();
                case 6 -> completeTask();
                case 7 -> reopenTask();
                case 8 -> deleteTask();
                case 9 -> inTaskMenu = false;
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void printTaskMenu() {
        System.out.println("\n--- Task Management ---");
        System.out.println("1. Create Task");
        System.out.println("2. View Task by ID");
        System.out.println("3. View All Tasks");
        System.out.println("4. View Tasks by Status");
        System.out.println("5. Start Task");
        System.out.println("6. Complete Task");
        System.out.println("7. Reopen Task");
        System.out.println("8. Delete Task");
        System.out.println("9. Back to Main Menu");
    }

    private void createTask() {
        try {
            System.out.println("\n--- Create Task ---");

            String name = readLine("Enter task name: ");
            String description = readLine("Enter task description: ");

            System.out.println("Select priority:");
            System.out.println("1. LOW");
            System.out.println("2. MEDIUM");
            System.out.println("3. HIGH");
            int priorityChoice = readInt("Enter priority choice: ");
            TaskPriority priority = switch (priorityChoice) {
                case 1 -> TaskPriority.LOW;
                case 2 -> TaskPriority.MEDIUM;
                case 3 -> TaskPriority.HIGH;
                default -> throw new IllegalArgumentException("Invalid priority choice.");
            };

            System.out.println("Select category:");
            System.out.println("1. STUDY");
            System.out.println("2. WORK");
            System.out.println("3. RECREATION");
            System.out.println("4. OTHERS");
            int categoryChoice = readInt("Enter category choice: ");
            TaskCategory category = switch (categoryChoice) {
                case 1 -> TaskCategory.STUDY;
                case 2 -> TaskCategory.WORK;
                case 3 -> TaskCategory.RECREATION;
                case 4 -> TaskCategory.OTHERS;
                default -> throw new IllegalArgumentException("Invalid category choice.");
            };

            String deadlineInput = readLine("Enter deadline (YYYY-MM-DD): ");
            LocalDate deadline = LocalDate.parse(deadlineInput);

            Task createdTask = taskController.createTask(name, description, priority, category, deadline);

            System.out.println("Task created successfully:");
            System.out.println(createdTask);

        } catch (Exception e) {
            System.out.println("Error creating task: " + e.getMessage());
        }
    }

    private void viewTaskById() {
        try {
            System.out.println("\n--- View Task by ID ---");
            Long taskId = readLong("Enter task ID: ");

            Optional<Task> taskOptional = taskController.getTaskById(taskId);

            if (taskOptional.isPresent()) {
                System.out.println("Task found:");
                System.out.println(taskOptional.get());
            } else {
                System.out.println("Task not found.");
            }

        } catch (Exception e) {
            System.out.println("Error retrieving task: " + e.getMessage());
        }
    }

    private void viewAllTasks() {
        try {
            System.out.println("\n--- All Tasks ---");
            List<Task> tasks = taskController.getAllTasks();

            if (tasks.isEmpty()) {
                System.out.println("No tasks found.");
            } else {
                tasks.forEach(System.out::println);
            }

        } catch (Exception e) {
            System.out.println("Error retrieving tasks: " + e.getMessage());
        }
    }

    private void viewTasksByStatus() {
        try {
            System.out.println("\n--- View Tasks by Status ---");
            System.out.println("1. TODO");
            System.out.println("2. IN_PROGRESS");
            System.out.println("3. COMPLETED");
            System.out.println("4. BACKLOG");

            int statusChoice = readInt("Enter status choice: ");
            TaskStatus status = switch (statusChoice) {
                case 1 -> TaskStatus.TODO;
                case 2 -> TaskStatus.IN_PROGRESS;
                case 3 -> TaskStatus.COMPLETED;
                case 4 -> TaskStatus.BACKLOG;
                default -> throw new IllegalArgumentException("Invalid status choice.");
            };

            List<Task> tasks = taskController.getTasksByStatus(status);

            if (tasks.isEmpty()) {
                System.out.println("No tasks found with status: " + status);
            } else {
                tasks.forEach(System.out::println);
            }

        } catch (Exception e) {
            System.out.println("Error retrieving tasks by status: " + e.getMessage());
        }
    }

    private void startTask() {
        try {
            System.out.println("\n--- Start Task ---");
            Long taskId = readLong("Enter task ID: ");

            Task updatedTask = taskController.startTask(taskId);
            System.out.println("Task started successfully:");
            System.out.println(updatedTask);

        } catch (Exception e) {
            System.out.println("Error starting task: " + e.getMessage());
        }
    }

    private void completeTask() {
        try {
            System.out.println("\n--- Complete Task ---");
            Long taskId = readLong("Enter task ID: ");

            Task updatedTask = taskController.completeTask(taskId);
            System.out.println("Task completed successfully:");
            System.out.println(updatedTask);

        } catch (Exception e) {
            System.out.println("Error completing task: " + e.getMessage());
        }
    }

    private void reopenTask() {
        try {
            System.out.println("\n--- Reopen Task ---");
            Long taskId = readLong("Enter task ID: ");

            Task updatedTask = taskController.reopenTask(taskId);
            System.out.println("Task reopened successfully:");
            System.out.println(updatedTask);

        } catch (Exception e) {
            System.out.println("Error reopening task: " + e.getMessage());
        }
    }

    private void deleteTask() {
        try {
            System.out.println("\n--- Delete Task ---");
            Long taskId = readLong("Enter task ID: ");

            boolean deleted = taskController.deleteTask(taskId);

            if (deleted) {
                System.out.println("Task deleted successfully.");
            } else {
                System.out.println("Task not found.");
            }

        } catch (Exception e) {
            System.out.println("Error deleting task: " + e.getMessage());
        }
    }

    private int readInt(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    private long readLong(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Long.parseLong(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid ID number.");
            }
        }
    }

    private String readLine(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }
}