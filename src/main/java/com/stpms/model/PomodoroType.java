package com.stpms.model;

public enum PomodoroType {

    FOCUS(25),
    SHORT_BREAK(5),
    LONG_BREAK(15),
    CUSTOM(0);

    private final int defaultDuration;

    PomodoroType(int defaultDuration) {
        this.defaultDuration = defaultDuration;
    }

    public int getDefaultDuration() {
        return defaultDuration;
    }
}