package com.stpms.controller;

import com.stpms.model.PomodoroSession;
import com.stpms.model.PomodoroType;
import com.stpms.service.PomodoroSessionService;

import java.util.List;
import java.util.Optional;

public class PomodoroSessionController {

    private final PomodoroSessionService sessionService;

    public PomodoroSessionController(PomodoroSessionService sessionService) {
        this.sessionService = sessionService;
    }

    public PomodoroSession createSession(Long taskId, PomodoroType type, int plannedMinutes) {
        return sessionService.createSession(taskId, type, plannedMinutes);
    }

    public Optional<PomodoroSession> getSessionById(Long sessionId) {
        return sessionService.getSessionById(sessionId);
    }

    public List<PomodoroSession> getAllSessions() {
        return sessionService.getAllSessions();
    }

    public List<PomodoroSession> getSessionsByTaskId(Long taskId) {
        return sessionService.getSessionsByTaskId(taskId);
    }

    public PomodoroSession updateSession(PomodoroSession session) {
        return sessionService.updateSession(session);
    }

    public PomodoroSession startSession(Long sessionId) {
        return sessionService.startSession(sessionId);
    }

    public PomodoroSession completeSession(Long sessionId) {
        return sessionService.completeSession(sessionId);
    }

    public boolean deleteSession(Long sessionId) {
        return sessionService.deleteSession(sessionId);
    }
}