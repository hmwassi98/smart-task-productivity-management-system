package com.stpms.service;

import com.stpms.config.DatabaseConnection;
import com.stpms.model.PomodoroSession;
import com.stpms.model.PomodoroType;
import com.stpms.model.Task;
import com.stpms.model.TaskCategory;
import com.stpms.model.TaskPriority;
import com.stpms.repository.JdbcPomodoroSessionRepository;
import com.stpms.repository.JdbcTaskRepository;
import com.stpms.repository.PomodoroSessionRepository;
import com.stpms.repository.TaskRepository;

import java.sql.Connection;
import java.time.LocalDate;

public class PomodoroSessionServiceTest {

    public static void main(String[] args) {
        try (Connection connection = DatabaseConnection.getConnection()) {

            TaskRepository taskRepository = new JdbcTaskRepository(connection);
            PomodoroSessionRepository sessionRepository = new JdbcPomodoroSessionRepository(connection);

            TaskService taskService = new TaskServiceImpl(taskRepository);
            PomodoroSessionService sessionService =
                    new PomodoroSessionServiceImpl(sessionRepository, taskRepository);

            Task parentTask = taskService.createTask(
                    "Parent Task for Pomodoro Service Test",
                    "Testing pomodoro session service methods",
                    TaskPriority.MEDIUM,
                    TaskCategory.STUDY,
                    LocalDate.now().plusDays(2)
            );

            System.out.println("Created parent task:");
            System.out.println(parentTask);

            PomodoroSession createdSession = sessionService.createSession(
                    parentTask.getTaskId(),
                    PomodoroType.FOCUS,
                    25
            );

            System.out.println("\nCreated session:");
            System.out.println(createdSession);
            System.out.println("Generated ID: " + createdSession.getSessionId());

            System.out.println("\nFinding session by ID...");
            sessionService.getSessionById(createdSession.getSessionId())
                    .ifPresentOrElse(
                            foundSession -> {
                                System.out.println("Session found:");
                                System.out.println(foundSession);
                            },
                            () -> System.out.println("Session not found.")
                    );

            System.out.println("\nAll sessions:");
            sessionService.getAllSessions().forEach(System.out::println);

            System.out.println("\nSessions by task ID:");
            sessionService.getSessionsByTaskId(parentTask.getTaskId()).forEach(System.out::println);

            System.out.println("\nCompleting session...");
            PomodoroSession completedSession = sessionService.completeSession(createdSession.getSessionId());
            System.out.println(completedSession);

            completedSession.setPlannedMinutes(30);
            PomodoroSession updatedSession = sessionService.updateSession(completedSession);

            System.out.println("\nUpdated session:");
            System.out.println(updatedSession);

            boolean deleted = sessionService.deleteSession(createdSession.getSessionId());
            System.out.println("\nDeleted session: " + deleted);

            System.out.println("Finding session after delete...");
            sessionService.getSessionById(createdSession.getSessionId())
                    .ifPresentOrElse(
                            session -> System.out.println("Session still exists: " + session),
                            () -> System.out.println("Session no longer exists.")
                    );

            taskService.deleteTask(parentTask.getTaskId());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}