package com.stpms.model;

import java.util.Objects;

public class Subtask {
    private Long subtaskId;
    private String name;
    private boolean completed;
    private Long parentTaskId;

    public Subtask(String name) {
        this.name = Objects.requireNonNull(name, "Subtask name cannot be null");
        this.completed = false;
        this.parentTaskId = null;
    }

    public Subtask(Long subtaskId, String name, boolean completed, Long parentTaskId) {
        this.subtaskId = subtaskId;
        this.name = Objects.requireNonNull(name, "Subtask name cannot be null");
        this.completed = completed;
        this.parentTaskId = parentTaskId;
    }

    public void markCompleted() {
        this.completed = true;
    }

    public void markIncomplete() {
        this.completed = false;
    }

    public Long getSubtaskId() {
        return subtaskId;
    }

    public void setSubtaskId(Long subtaskId) {
        this.subtaskId = subtaskId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = Objects.requireNonNull(name, "Subtask name cannot be null");
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public Long getParentTaskId() {
        return parentTaskId;
    }

    public void setParentTaskId(Long parentTaskId) {
        this.parentTaskId = parentTaskId;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "subtaskId=" + subtaskId +
                ", name='" + name + '\'' +
                ", completed=" + completed +
                ", parentTaskId=" + parentTaskId +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Subtask subtask)) return false;
        return subtaskId != null && subtaskId.equals(subtask.subtaskId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(subtaskId);
    }
}