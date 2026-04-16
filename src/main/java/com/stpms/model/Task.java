package com.stpms.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Task {
    private long taskId;
    private String name;
    private String description;
    private TaskStatus status;
    private TaskPriority priority;
    private TaskCategory category;
    private LocalDateTime createdAt;
    private LocalDateTime completedAt;
    private LocalDate deadline;
    private List<Subtask> subtasks;


//    private String TaskDueDate;
//    private String TaskDueTime;
//    private String TaskDueTimeStart;
//    private String TaskDueTimeEnd;
//    private String TaskDueTimeStartEnd;
//    private String TaskDueTimeEndStart;
//    private String TaskDueTimeEndEnd;


    public Task(String name, String description, TaskPriority priority, LocalDate deadline) {
        this.name = name;
        this.description = description;
        this.priority = priority;
        this.deadline = deadline;

        this.status = TaskStatus.TODO;
        this.createdAt = LocalDateTime.now();
        this.subtasks = new ArrayList<>();
    }

    public Task(Long id, String name, String description, TaskPriority priority,
                TaskStatus status, LocalDate deadline, LocalDateTime createdAt) {

        this.taskId = id;
        this.name = name;
        this.description = description;
        this.priority = priority;
        this.status = status;
        this.deadline = deadline;
        this.createdAt = createdAt;
        this.subtasks = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + taskId +
                ", title='" + name + '\'' +
                ", status=" + status +
                ", priority=" + priority +
                '}';
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void startTask() {
        if (status == TaskStatus.TODO || status == TaskStatus.BACKLOG) {
            status = TaskStatus.IN_PROGRESS;
        }
    }

    public void reopenTask() {
        status = TaskStatus.IN_PROGRESS;
        completedAt = null;
    }

    public void markAsCompleted() {
        if (this.status != TaskStatus.COMPLETED) {
            this.status = TaskStatus.COMPLETED;
            this.completedAt = LocalDateTime.now();
        }
    }

    public boolean isCompleted() {
        return this.status == TaskStatus.COMPLETED;
    }

    public boolean isOverdue() {
        if (deadline == null) {
            return false;
        }

        return LocalDate.now().isAfter(deadline);
    }

    public List<Subtask> getSubTasks() {
        return this.subtasks;
    }

    public long getTaskId() {
        return taskId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public TaskPriority getPriority() {
        return priority;
    }

    public TaskCategory getTaskCategory() {
        return category;
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

    public List<Subtask> getSubtasks() {
        return subtasks;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPriority(TaskPriority priority) {
        this.priority = priority;
    }

    public void setTaskCategory(TaskCategory category) {
        this.category = category;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return taskId == task.taskId;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(taskId);
    }
}