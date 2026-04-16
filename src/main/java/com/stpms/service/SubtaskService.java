package com.stpms.service;

import com.stpms.model.Subtask;

import java.util.List;
import java.util.Optional;

public interface SubtaskService {

    Subtask createSubtask(Long taskId, String name);

    Optional<Subtask> getSubtaskById(Long subtaskId);

    List<Subtask> getAllSubtasks();

    List<Subtask> getSubtasksByTaskId(Long taskId);

    Subtask updateSubtask(Subtask subtask);

    Subtask completeSubtask(Long subtaskId);

    Subtask reopenSubtask(Long subtaskId);

    boolean deleteSubtask(Long subtaskId);
}