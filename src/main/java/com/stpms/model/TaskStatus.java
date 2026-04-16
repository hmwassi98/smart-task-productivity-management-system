package com.stpms.model;

public enum TaskStatus {

    BACKLOG,
    TODO,
    IN_PROGRESS,
    COMPLETED;

    public boolean isDone() {
        return this == COMPLETED;
    }

    public boolean isActive() {
        return this == IN_PROGRESS;
    }

    public boolean isPending() {
        return this == TODO || this == BACKLOG;
    }
}