package com.stpms.model;

public class Subtask {
    private Long subtaskId;
    private String name;
    private boolean completed;
    private Long parentTaskID;   // Foreign key reference

    public Subtask(String name) {
        this.name = name;
        this.completed = false;
        this.parentTaskID = null;
    }

    public void markCompleted() {
        this.completed = true;
    }

    public boolean isCompleted() {
        return completed;
    }
}
