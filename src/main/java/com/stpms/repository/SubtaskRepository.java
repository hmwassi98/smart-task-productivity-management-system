package com.stpms.repository;

import com.stpms.model.Subtask;

import java.util.List;
import java.util.Optional;

public interface SubtaskRepository {

    Subtask save(Subtask subtask);

    Optional<Subtask> findById(Long subtaskId);

    List<Subtask> findAll();

    List<Subtask> findByTaskId(Long taskId);

    Subtask update(Subtask subtask);

    boolean deleteById(Long subtaskId);

    boolean existsById(Long subtaskId);
}