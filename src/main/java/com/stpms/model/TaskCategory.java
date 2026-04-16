package com.stpms.model;

public enum TaskCategory {

    STUDY("Study"),
    WORK("Work"),
    RECREATION("Recreation");

    private final String displayName;

    TaskCategory(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}