package com.stpms;

import com.stpms.config.DatabaseConnection;
import com.stpms.controller.PomodoroSessionController;
import com.stpms.controller.SubtaskController;
import com.stpms.controller.TaskController;
import com.stpms.controller.UserController;
import com.stpms.repository.*;
import com.stpms.service.*;

import java.sql.Connection;
import java.sql.SQLException;

public class Application {

    private final Connection connection;

    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final SubtaskRepository subtaskRepository;
    private final PomodoroSessionRepository pomodoroSessionRepository;

    private final UserService userService;
    private final TaskService taskService;
    private final SubtaskService subtaskService;
    private final PomodoroSessionService pomodoroSessionService;

    private final UserController userController;
    private final TaskController taskController;
    private final SubtaskController subtaskController;
    private final PomodoroSessionController pomodoroSessionController;

    public Application() throws SQLException {
        this.connection = DatabaseConnection.getConnection();

        this.userRepository = new JdbcUserRepository(connection);
        this.taskRepository = new JdbcTaskRepository(connection);
        this.subtaskRepository = new JdbcSubtaskRepository(connection);
        this.pomodoroSessionRepository = new JdbcPomodoroSessionRepository(connection);

        this.userService = new UserServiceImpl(userRepository);
        this.taskService = new TaskServiceImpl(taskRepository);
        this.subtaskService = new SubtaskServiceImpl(subtaskRepository, taskRepository);
        this.pomodoroSessionService = new PomodoroSessionServiceImpl(pomodoroSessionRepository, taskRepository);

        this.userController = new UserController(userService);
        this.taskController = new TaskController(taskService);
        this.subtaskController = new SubtaskController(subtaskService);
        this.pomodoroSessionController = new PomodoroSessionController(pomodoroSessionService);
    }

    public UserController getUserController() {
        return userController;
    }

    public TaskController getTaskController() {
        return taskController;
    }

    public SubtaskController getSubtaskController() {
        return subtaskController;
    }

    public PomodoroSessionController getPomodoroSessionController() {
        return pomodoroSessionController;
    }

    public void shutdown() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Application shutdown complete. Database connection closed.");
            }
        } catch (SQLException e) {
            System.err.println("Error while closing database connection: " + e.getMessage());
        }
    }
}