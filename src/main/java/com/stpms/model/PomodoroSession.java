package com.stpms.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class PomodoroSession {
    private Long sessionId;
    private Long taskId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private int plannedMinutes;
    private Integer actualMinutes;
    private boolean completed;
    private PomodoroType type;

    public PomodoroSession(Long taskId, PomodoroType type, int plannedMinutes) {
        this.taskId = Objects.requireNonNull(taskId, "Task ID cannot be null");
        this.type = Objects.requireNonNull(type, "Pomodoro type cannot be null");
        setPlannedMinutes(plannedMinutes);
        this.completed = false;
    }

    public PomodoroSession(Long sessionId, Long taskId, LocalDateTime startTime,
                           LocalDateTime endTime, int plannedMinutes,
                           Integer actualMinutes, boolean completed, PomodoroType type) {
        this.sessionId = sessionId;
        this.taskId = Objects.requireNonNull(taskId, "Task ID cannot be null");
        this.startTime = startTime;
        this.endTime = endTime;
        setPlannedMinutes(plannedMinutes);
        setActualMinutes(actualMinutes);
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
            actualMinutes = (int) Duration.between(startTime, endTime).toMinutes();
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
        this.taskId = Objects.requireNonNull(taskId, "Task ID cannot be null");
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

    public int getPlannedMinutes() {
        return plannedMinutes;
    }

    public void setPlannedMinutes(int plannedMinutes) {
        if (plannedMinutes <= 0) {
            throw new IllegalArgumentException("Planned minutes must be greater than 0.");
        }
        this.plannedMinutes = plannedMinutes;
    }

    public Integer getActualMinutes() {
        return actualMinutes;
    }

    public void setActualMinutes(Integer actualMinutes) {
        if (actualMinutes != null && actualMinutes < 0) {
            throw new IllegalArgumentException("Actual minutes cannot be negative.");
        }
        this.actualMinutes = actualMinutes;
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
                ", plannedMinutes=" + plannedMinutes +
                ", actualMinutes=" + actualMinutes +
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