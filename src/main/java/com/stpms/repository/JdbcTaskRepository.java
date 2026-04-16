package com.stpms.repository;

import com.stpms.model.Task;
import com.stpms.model.TaskCategory;
import com.stpms.model.TaskPriority;
import com.stpms.model.TaskStatus;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcTaskRepository implements TaskRepository {

    private final Connection connection;

    public JdbcTaskRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Task save(Task task) {
        String sql = """
                INSERT INTO tasks (user_id, name, description, status, priority, category, created_at, completed_at, deadline)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            if (task.getUserId() != null) {
                ps.setLong(1, task.getUserId());
            } else {
                ps.setNull(1, java.sql.Types.BIGINT);
            }

            ps.setString(2, task.getName());
            ps.setString(3, task.getDescription());
            ps.setString(4, task.getStatus().name());
            ps.setString(5, task.getPriority().name());

            if (task.getCategory() != null) {
                ps.setString(6, task.getCategory().name());
            } else {
                ps.setNull(6, java.sql.Types.VARCHAR);
            }

            ps.setTimestamp(7, Timestamp.valueOf(task.getCreatedAt()));

            if (task.getCompletedAt() != null) {
                ps.setTimestamp(8, Timestamp.valueOf(task.getCompletedAt()));
            } else {
                ps.setNull(8, java.sql.Types.TIMESTAMP);
            }

            if (task.getDeadline() != null) {
                ps.setDate(9, Date.valueOf(task.getDeadline()));
            } else {
                ps.setNull(9, java.sql.Types.DATE);
            }

            int affectedRows = ps.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Saving task failed, no rows affected.");
            }

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    task.setTaskId(rs.getLong(1));
                } else {
                    throw new SQLException("Saving task failed, no ID obtained.");
                }
            }

            return task;

        } catch (SQLException e) {
            throw new RuntimeException("Error saving task", e);
        }
    }

    @Override
    public Optional<Task> findById(Long taskId) {
        String sql = "SELECT * FROM tasks WHERE task_id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, taskId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRowToTask(rs));
                }
            }

            return Optional.empty();

        } catch (SQLException e) {
            throw new RuntimeException("Error finding task by ID: " + taskId, e);
        }
    }

    @Override
    public List<Task> findAll() {
        String sql = "SELECT * FROM tasks";
        List<Task> tasks = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                tasks.add(mapRowToTask(rs));
            }

            return tasks;

        } catch (SQLException e) {
            throw new RuntimeException("Error finding all tasks", e);
        }
    }

    @Override
    public List<Task> findByUserId(Long userId) {
        String sql = "SELECT * FROM tasks WHERE user_id = ?";
        List<Task> tasks = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    tasks.add(mapRowToTask(rs));
                }
            }

            return tasks;

        } catch (SQLException e) {
            throw new RuntimeException("Error finding tasks by user ID: " + userId, e);
        }
    }

    @Override
    public List<Task> findByStatus(TaskStatus status) {
        String sql = "SELECT * FROM tasks WHERE status = ?";
        List<Task> tasks = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, status.name());

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    tasks.add(mapRowToTask(rs));
                }
            }

            return tasks;

        } catch (SQLException e) {
            throw new RuntimeException("Error finding tasks by status: " + status, e);
        }
    }

    @Override
    public Task update(Task task) {
        String sql = """
                UPDATE tasks
                SET user_id = ?, name = ?, description = ?, status = ?, priority = ?, category = ?, completed_at = ?, deadline = ?
                WHERE task_id = ?
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            if (task.getUserId() != null) {
                ps.setLong(1, task.getUserId());
            } else {
                ps.setNull(1, java.sql.Types.BIGINT);
            }

            ps.setString(2, task.getName());
            ps.setString(3, task.getDescription());
            ps.setString(4, task.getStatus().name());
            ps.setString(5, task.getPriority().name());

            if (task.getCategory() != null) {
                ps.setString(6, task.getCategory().name());
            } else {
                ps.setNull(6, java.sql.Types.VARCHAR);
            }

            if (task.getCompletedAt() != null) {
                ps.setTimestamp(7, Timestamp.valueOf(task.getCompletedAt()));
            } else {
                ps.setNull(7, java.sql.Types.TIMESTAMP);
            }

            if (task.getDeadline() != null) {
                ps.setDate(8, Date.valueOf(task.getDeadline()));
            } else {
                ps.setNull(8, java.sql.Types.DATE);
            }

            ps.setLong(9, task.getTaskId());

            int affectedRows = ps.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Updating task failed, no rows affected.");
            }

            return task;

        } catch (SQLException e) {
            throw new RuntimeException("Error updating task with ID: " + task.getTaskId(), e);
        }
    }

    @Override
    public boolean deleteById(Long taskId) {
        String sql = "DELETE FROM tasks WHERE task_id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, taskId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Error deleting task with ID: " + taskId, e);
        }
    }

    @Override
    public boolean existsById(Long taskId) {
        String sql = "SELECT 1 FROM tasks WHERE task_id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, taskId);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error checking existence of task with ID: " + taskId, e);
        }
    }

    private Task mapRowToTask(ResultSet rs) throws SQLException {
        Long taskId = rs.getLong("task_id");
        String name = rs.getString("name");
        String description = rs.getString("description");

        TaskPriority priority = TaskPriority.valueOf(rs.getString("priority"));
        TaskStatus status = TaskStatus.valueOf(rs.getString("status"));

        String categoryValue = rs.getString("category");
        TaskCategory category = categoryValue != null ? TaskCategory.valueOf(categoryValue) : null;

        Date deadlineDate = rs.getDate("deadline");
        Timestamp createdAtTimestamp = rs.getTimestamp("created_at");
        Timestamp completedAtTimestamp = rs.getTimestamp("completed_at");

        Task task = new Task(
                taskId,
                name,
                description,
                priority,
                status,
                category,
                deadlineDate != null ? deadlineDate.toLocalDate() : null,
                createdAtTimestamp != null ? createdAtTimestamp.toLocalDateTime() : null,
                completedAtTimestamp != null ? completedAtTimestamp.toLocalDateTime() : null
        );

        long userIdValue = rs.getLong("user_id");
        if (!rs.wasNull()) {
            task.setUserId(userIdValue);
        }

        return task;
    }
}