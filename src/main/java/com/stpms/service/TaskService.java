package com.stpms.service;

import com.stpms.model.Task;
import com.stpms.model.TaskCategory;
import com.stpms.model.TaskPriority;
import com.stpms.model.TaskStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TaskService {

    Task createTask(String name, String description, TaskPriority priority,
                    TaskCategory category, LocalDate deadline);

    Optional<Task> getTaskById(Long taskId);

    List<Task> getAllTasks();

    List<Task> getTasksByUserId(Long userId);

    List<Task> getTasksByStatus(TaskStatus status);

    Task updateTask(Task task);

    Task startTask(Long taskId);

    Task completeTask(Long taskId);

    Task reopenTask(Long taskId);

    boolean deleteTask(Long taskId);
}