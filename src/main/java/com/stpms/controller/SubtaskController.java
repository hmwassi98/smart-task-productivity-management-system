package com.stpms.controller;

import com.stpms.model.Subtask;
import com.stpms.service.SubtaskService;

import java.util.List;
import java.util.Optional;

public class SubtaskController {

    private final SubtaskService subtaskService;

    public SubtaskController(SubtaskService subtaskService) {
        this.subtaskService = subtaskService;
    }

    public Subtask createSubtask(Long taskId, String name) {
        return subtaskService.createSubtask(taskId, name);
    }

    public Optional<Subtask> getSubtaskById(Long subtaskId) {
        return subtaskService.getSubtaskById(subtaskId);
    }

    public List<Subtask> getAllSubtasks() {
        return subtaskService.getAllSubtasks();
    }

    public List<Subtask> getSubtasksByTaskId(Long taskId) {
        return subtaskService.getSubtasksByTaskId(taskId);
    }

    public Subtask updateSubtask(Subtask subtask) {
        return subtaskService.updateSubtask(subtask);
    }

    public Subtask completeSubtask(Long subtaskId) {
        return subtaskService.completeSubtask(subtaskId);
    }

    public Subtask reopenSubtask(Long subtaskId) {
        return subtaskService.reopenSubtask(subtaskId);
    }

    public boolean deleteSubtask(Long subtaskId) {
        return subtaskService.deleteSubtask(subtaskId);
    }
}