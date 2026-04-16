package com.stpms.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Task {
    private Long taskId;
    private String name;
    private String description;
    private TaskStatus status;
    private TaskPriority priority;
    private TaskCategory category;
    private LocalDateTime createdAt;
    private LocalDateTime completedAt;
    private LocalDate deadline;
    private final List<Subtask> subtasks;

    public Task(String name, String description, TaskPriority priority,
                TaskCategory category, LocalDate deadline) {
        this.name = Objects.requireNonNull(name, "Task name cannot be null");
        this.description = description;
        this.priority = Objects.requireNonNull(priority, "Task priority cannot be null");
        this.category = category;
        this.deadline = deadline;

        this.status = TaskStatus.TODO;
        this.createdAt = LocalDateTime.now();
        this.completedAt = null;
        this.subtasks = new ArrayList<>();
    }

    public Task(Long taskId, String name, String description, TaskPriority priority,
                TaskStatus status, TaskCategory category, LocalDate deadline,
                LocalDateTime createdAt, LocalDateTime completedAt) {
        this.taskId = taskId;
        this.name = Objects.requireNonNull(name, "Task name cannot be null");
        this.description = description;
        this.priority = Objects.requireNonNull(priority, "Task priority cannot be null");
        this.status = Objects.requireNonNull(status, "Task status cannot be null");
        this.category = category;
        this.deadline = deadline;
        this.createdAt = (createdAt != null) ? createdAt : LocalDateTime.now();
        this.completedAt = completedAt;
        this.subtasks = new ArrayList<>();
    }

    public void startTask() {
        if (status == TaskStatus.TODO || status == TaskStatus.BACKLOG) {
            status = TaskStatus.IN_PROGRESS;
        }
    }

    public void reopenTask() {
        if (status == TaskStatus.COMPLETED) {
            status = TaskStatus.IN_PROGRESS;
            completedAt = null;
        }
    }

    public void markAsCompleted() {
        if (status != TaskStatus.COMPLETED) {
            status = TaskStatus.COMPLETED;
            completedAt = LocalDateTime.now();
        }
    }

    public boolean isCompleted() {
        return status == TaskStatus.COMPLETED;
    }

    public boolean isOverdue() {
        return deadline != null
                && !isCompleted()
                && LocalDate.now().isAfter(deadline);
    }

    public void addSubtask(Subtask subtask) {
        if (subtask != null) {
            subtasks.add(subtask);
        }
    }

    public void removeSubtask(Subtask subtask) {
        subtasks.remove(subtask);
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = Objects.requireNonNull(name, "Task name cannot be null");
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = Objects.requireNonNull(status, "Task status cannot be null");
    }

    public TaskPriority getPriority() {
        return priority;
    }

    public void setPriority(TaskPriority priority) {
        this.priority = Objects.requireNonNull(priority, "Task priority cannot be null");
    }

    public TaskCategory getCategory() {
        return category;
    }

    public void setCategory(TaskCategory category) {
        this.category = category;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    public List<Subtask> getSubtasks() {
        return subtasks;
    }

    @Override
    public String toString() {
        return "Task{" +
                "taskId=" + taskId +
                ", name='" + name + '\'' +
                ", status=" + status +
                ", priority=" + priority +
                ", category=" + category +
                ", deadline=" + deadline +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Task task)) return false;
        return taskId != null && taskId.equals(task.taskId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(taskId);
    }
}