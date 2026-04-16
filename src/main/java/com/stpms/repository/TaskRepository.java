package com.stpms.repository;

import com.stpms.model.Task;
import com.stpms.model.TaskStatus;

import java.util.List;
import java.util.Optional;

public interface TaskRepository {

    Task save(Task task);

    Optional<Task> findById(Long taskId);

    List<Task> findAll();

    List<Task> findByUserId(Long userId);

    List<Task> findByStatus(TaskStatus status);

    Task update(Task task);

    boolean deleteById(Long taskId);

    boolean existsById(Long taskId);
}