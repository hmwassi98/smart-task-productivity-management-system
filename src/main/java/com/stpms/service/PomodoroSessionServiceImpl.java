package com.stpms.service;

import com.stpms.model.PomodoroSession;
import com.stpms.model.PomodoroType;
import com.stpms.repository.PomodoroSessionRepository;
import com.stpms.repository.TaskRepository;

import java.util.List;
import java.util.Optional;

public class PomodoroSessionServiceImpl implements PomodoroSessionService {

    private final PomodoroSessionRepository sessionRepository;
    private final TaskRepository taskRepository;

    public PomodoroSessionServiceImpl(PomodoroSessionRepository sessionRepository,
                                      TaskRepository taskRepository) {
        this.sessionRepository = sessionRepository;
        this.taskRepository = taskRepository;
    }

    @Override
    public PomodoroSession createSession(Long taskId, PomodoroType type, int plannedMinutes) {
        if (!taskRepository.existsById(taskId)) {
            throw new IllegalArgumentException("Task with ID " + taskId + " does not exist.");
        }

        PomodoroSession session = new PomodoroSession(taskId, type, plannedMinutes);
        session.startSession();

        return sessionRepository.save(session);
    }

    @Override
    public Optional<PomodoroSession> getSessionById(Long sessionId) {
        return sessionRepository.findById(sessionId);
    }

    @Override
    public List<PomodoroSession> getAllSessions() {
        return sessionRepository.findAll();
    }

    @Override
    public List<PomodoroSession> getSessionsByTaskId(Long taskId) {
        return sessionRepository.findByTaskId(taskId);
    }

    @Override
    public PomodoroSession updateSession(PomodoroSession session) {
        if (session.getSessionId() == null) {
            throw new IllegalArgumentException("Session ID cannot be null when updating.");
        }

        if (!sessionRepository.existsById(session.getSessionId())) {
            throw new IllegalArgumentException("Session with ID " + session.getSessionId() + " does not exist.");
        }

        return sessionRepository.update(session);
    }

    @Override
    public PomodoroSession startSession(Long sessionId) {
        PomodoroSession session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("Session with ID " + sessionId + " not found."));

        if (session.getStartTime() == null) {
            session.startSession();
        }

        return sessionRepository.update(session);
    }

    @Override
    public PomodoroSession completeSession(Long sessionId) {
        PomodoroSession session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("Session with ID " + sessionId + " not found."));

        session.endSession(true);
        return sessionRepository.update(session);
    }

    @Override
    public boolean deleteSession(Long sessionId) {
        if (!sessionRepository.existsById(sessionId)) {
            return false;
        }

        return sessionRepository.deleteById(sessionId);
    }
}