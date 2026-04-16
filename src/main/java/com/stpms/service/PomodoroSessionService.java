package com.stpms.service;

import com.stpms.model.PomodoroSession;
import com.stpms.model.PomodoroType;

import java.util.List;
import java.util.Optional;

public interface PomodoroSessionService {

    PomodoroSession createSession(Long taskId, PomodoroType type, int plannedMinutes);

    Optional<PomodoroSession> getSessionById(Long sessionId);

    List<PomodoroSession> getAllSessions();

    List<PomodoroSession> getSessionsByTaskId(Long taskId);

    PomodoroSession updateSession(PomodoroSession session);

    PomodoroSession startSession(Long sessionId);

    PomodoroSession completeSession(Long sessionId);

    boolean deleteSession(Long sessionId);
}