package com.stpms.controller;

import com.stpms.model.Task;
import com.stpms.model.TaskCategory;
import com.stpms.model.TaskPriority;
import com.stpms.model.TaskStatus;
import com.stpms.service.TaskService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    public Task createTask(String name, String description, TaskPriority priority,
                           TaskCategory category, LocalDate deadline) {
        return taskService.createTask(name, description, priority, category, deadline);
    }

    public Optional<Task> getTaskById(Long taskId) {
        return taskService.getTaskById(taskId);
    }

    public List<Task> getAllTasks() {
        return taskService.getAllTasks();
    }

    public List<Task> getTasksByUserId(Long userId) {
        return taskService.getTasksByUserId(userId);
    }

    public List<Task> getTasksByStatus(TaskStatus status) {
        return taskService.getTasksByStatus(status);
    }

    public Task updateTask(Task task) {
        return taskService.updateTask(task);
    }

    public Task startTask(Long taskId) {
        return taskService.startTask(taskId);
    }

    public Task completeTask(Long taskId) {
        return taskService.completeTask(taskId);
    }

    public Task reopenTask(Long taskId) {
        return taskService.reopenTask(taskId);
    }

    public boolean deleteTask(Long taskId) {
        return taskService.deleteTask(taskId);
    }
}