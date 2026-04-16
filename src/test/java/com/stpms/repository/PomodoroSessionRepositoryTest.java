package com.stpms.repository;

import com.stpms.config.DatabaseConnection;
import com.stpms.model.PomodoroSession;
import com.stpms.model.PomodoroType;
import com.stpms.model.Task;
import com.stpms.model.TaskCategory;
import com.stpms.model.TaskPriority;

import java.sql.Connection;
import java.time.LocalDate;

public class PomodoroSessionRepositoryTest {

    public static void main(String[] args) {
        try (Connection connection = DatabaseConnection.getConnection()) {

            TaskRepository taskRepository = new JdbcTaskRepository(connection);
            PomodoroSessionRepository sessionRepository = new JdbcPomodoroSessionRepository(connection);

            Task task = new Task(
                    "Pomodoro Parent Task",
                    "Used to test pomodoro sessions",
                    TaskPriority.MEDIUM,
                    TaskCategory.STUDY,
                    LocalDate.now().plusDays(2)
            );

            Task savedTask = taskRepository.save(task);

            System.out.println("Saved parent task:");
            System.out.println(savedTask);
            System.out.println("Parent Task ID: " + savedTask.getTaskId());

            PomodoroSession session = new PomodoroSession(
                    savedTask.getTaskId(),
                    PomodoroType.FOCUS,
                    25
            );

            session.startSession();

            PomodoroSession savedSession = sessionRepository.save(session);

            System.out.println("\nSaved pomodoro session:");
            System.out.println(savedSession);
            System.out.println("Generated Session ID: " + savedSession.getSessionId());

            System.out.println("\nFinding session by ID...");
            sessionRepository.findById(savedSession.getSessionId())
                    .ifPresentOrElse(
                            foundSession -> {
                                System.out.println("Session found:");
                                System.out.println(foundSession);
                            },
                            () -> System.out.println("Session not found.")
                    );

            System.out.println("\nAll sessions:");
            sessionRepository.findAll().forEach(System.out::println);

            System.out.println("\nSessions by task ID:");
            sessionRepository.findByTaskId(savedTask.getTaskId()).forEach(System.out::println);

            savedSession.setCompleted(true);
            PomodoroSession updatedSession = sessionRepository.update(savedSession);

            System.out.println("\nUpdated session:");
            System.out.println(updatedSession);

            System.out.println("\nExists by ID:");
            System.out.println(sessionRepository.existsById(savedSession.getSessionId()));

            boolean deleted = sessionRepository.deleteById(savedSession.getSessionId());
            System.out.println("\nDeleted: " + deleted);

            System.out.println("Exists after delete:");
            System.out.println(sessionRepository.existsById(savedSession.getSessionId()));

            taskRepository.deleteById(savedTask.getTaskId());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}