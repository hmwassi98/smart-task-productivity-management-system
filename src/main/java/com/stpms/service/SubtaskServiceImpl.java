package com.stpms.service;

import com.stpms.model.Subtask;
import com.stpms.repository.SubtaskRepository;
import com.stpms.repository.TaskRepository;

import java.util.List;
import java.util.Optional;

public class SubtaskServiceImpl implements SubtaskService {

    private final SubtaskRepository subtaskRepository;
    private final TaskRepository taskRepository;

    public SubtaskServiceImpl(SubtaskRepository subtaskRepository, TaskRepository taskRepository) {
        this.subtaskRepository = subtaskRepository;
        this.taskRepository = taskRepository;
    }

    @Override
    public Subtask createSubtask(Long taskId, String name) {
        if (!taskRepository.existsById(taskId)) {
            throw new IllegalArgumentException("Task with ID " + taskId + " does not exist.");
        }

        Subtask subtask = new Subtask(name);
        subtask.setParentTaskId(taskId);

        return subtaskRepository.save(subtask);
    }

    @Override
    public Optional<Subtask> getSubtaskById(Long subtaskId) {
        return subtaskRepository.findById(subtaskId);
    }

    @Override
    public List<Subtask> getAllSubtasks() {
        return subtaskRepository.findAll();
    }

    @Override
    public List<Subtask> getSubtasksByTaskId(Long taskId) {
        return subtaskRepository.findByTaskId(taskId);
    }

    @Override
    public Subtask updateSubtask(Subtask subtask) {
        if (subtask.getSubtaskId() == null) {
            throw new IllegalArgumentException("Subtask ID cannot be null when updating.");
        }

        if (!subtaskRepository.existsById(subtask.getSubtaskId())) {
            throw new IllegalArgumentException("Subtask with ID " + subtask.getSubtaskId() + " does not exist.");
        }

        return subtaskRepository.update(subtask);
    }

    @Override
    public Subtask completeSubtask(Long subtaskId) {
        Subtask subtask = subtaskRepository.findById(subtaskId)
                .orElseThrow(() -> new IllegalArgumentException("Subtask with ID " + subtaskId + " not found."));

        subtask.setCompleted(true);
        return subtaskRepository.update(subtask);
    }

    @Override
    public Subtask reopenSubtask(Long subtaskId) {
        Subtask subtask = subtaskRepository.findById(subtaskId)
                .orElseThrow(() -> new IllegalArgumentException("Subtask with ID " + subtaskId + " not found."));

        subtask.setCompleted(false);
        return subtaskRepository.update(subtask);
    }

    @Override
    public boolean deleteSubtask(Long subtaskId) {
        if (!subtaskRepository.existsById(subtaskId)) {
            return false;
        }

        return subtaskRepository.deleteById(subtaskId);
    }
}