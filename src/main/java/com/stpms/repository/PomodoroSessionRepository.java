package com.stpms.repository;

import com.stpms.model.PomodoroSession;

import java.util.List;
import java.util.Optional;

public interface PomodoroSessionRepository {

    PomodoroSession save(PomodoroSession session);

    Optional<PomodoroSession> findById(Long sessionId);

    List<PomodoroSession> findAll();

    List<PomodoroSession> findByTaskId(Long taskId);

    PomodoroSession update(PomodoroSession session);

    boolean deleteById(Long sessionId);

    boolean existsById(Long sessionId);
}