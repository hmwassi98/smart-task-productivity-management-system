package com.stpms.service;

import com.stpms.model.Task;
import com.stpms.model.TaskCategory;
import com.stpms.model.TaskPriority;
import com.stpms.model.TaskStatus;
import com.stpms.repository.TaskRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public Task createTask(String name, String description, TaskPriority priority,
                           TaskCategory category, LocalDate deadline) {
        Task task = new Task(name, description, priority, category, deadline);
        return taskRepository.save(task);
    }

    @Override
    public Optional<Task> getTaskById(Long taskId) {
        return taskRepository.findById(taskId);
    }

    @Override
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    @Override
    public List<Task> getTasksByUserId(Long userId) {
        return taskRepository.findByUserId(userId);
    }

    @Override
    public List<Task> getTasksByStatus(TaskStatus status) {
        return taskRepository.findByStatus(status);
    }

    @Override
    public Task updateTask(Task task) {
        if (task.getTaskId() == null) {
            throw new IllegalArgumentException("Task ID cannot be null when updating.");
        }

        if (!taskRepository.existsById(task.getTaskId())) {
            throw new IllegalArgumentException("Task with ID " + task.getTaskId() + " does not exist.");
        }

        return taskRepository.update(task);
    }

    @Override
    public Task startTask(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task with ID " + taskId + " not found."));

        task.startTask();
        return taskRepository.update(task);
    }

    @Override
    public Task completeTask(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task with ID " + taskId + " not found."));

        task.markAsCompleted();
        return taskRepository.update(task);
    }

    @Override
    public Task reopenTask(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task with ID " + taskId + " not found."));

        task.reopenTask();
        return taskRepository.update(task);
    }

    @Override
    public boolean deleteTask(Long taskId) {
        if (!taskRepository.existsById(taskId)) {
            return false;
        }

        return taskRepository.deleteById(taskId);
    }
}