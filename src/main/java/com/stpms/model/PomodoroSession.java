package com.stpms.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class PomodoroSession {
    private Long sessionId;
    private Long taskId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private int durationMinutes;
    private boolean completed;
    private PomodoroType type;

    public PomodoroSession(Long taskId, PomodoroType type) {
        this.taskId = taskId;
        this.type = Objects.requireNonNull(type, "Pomodoro type cannot be null");
        this.completed = false;
    }

    public PomodoroSession(Long sessionId, Long taskId, LocalDateTime startTime,
                           LocalDateTime endTime, int durationMinutes,
                           boolean completed, PomodoroType type) {
        this.sessionId = sessionId;
        this.taskId = taskId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.durationMinutes = durationMinutes;
        this.completed = completed;
        this.type = Objects.requireNonNull(type, "Pomodoro type cannot be null");
    }

    public void startSession() {
        if (startTime == null) {
            startTime = LocalDateTime.now();
        }
    }

    public void endSession(boolean completed) {
        if (startTime == null) {
            throw new IllegalStateException("Session has not been started yet.");
        }

        if (endTime == null) {
            endTime = LocalDateTime.now();
            durationMinutes = (int) Duration.between(startTime, endTime).toMinutes();
            this.completed = completed;
        }
    }

    public boolean isInProgress() {
        return startTime != null && endTime == null;
    }

    public Long getSessionId() {
        return sessionId;
    }

    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(int durationMinutes) {
        if (durationMinutes < 0) {
            throw new IllegalArgumentException("Duration cannot be negative.");
        }
        this.durationMinutes = durationMinutes;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public PomodoroType getType() {
        return type;
    }

    public void setType(PomodoroType type) {
        this.type = Objects.requireNonNull(type, "Pomodoro type cannot be null");
    }

    @Override
    public String toString() {
        return "PomodoroSession{" +
                "sessionId=" + sessionId +
                ", taskId=" + taskId +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", durationMinutes=" + durationMinutes +
                ", completed=" + completed +
                ", type=" + type +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PomodoroSession that)) return false;
        return sessionId != null && sessionId.equals(that.sessionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sessionId);
    }
}