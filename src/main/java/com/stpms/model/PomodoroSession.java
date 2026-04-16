package com.stpms.model;

import java.time.LocalDateTime;

public class PomodoroSession {
    private Long sessionId;
    private Long taskId;            // Link to Task
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private int durationMinutes;
    private boolean completed;      // Did user finish session?
    private PomodoroType type;

}
