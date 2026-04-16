package com.stpms.repository;

import com.stpms.model.Subtask;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcSubtaskRepository implements SubtaskRepository {

    private final Connection connection;

    public JdbcSubtaskRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Subtask save(Subtask subtask) {
        String sql = """
                INSERT INTO subtasks (task_id, name, completed)
                VALUES (?, ?, ?)
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, subtask.getParentTaskId());
            ps.setString(2, subtask.getName());
            ps.setBoolean(3, subtask.isCompleted());

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Saving subtask failed, no rows affected.");
            }

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    subtask.setSubtaskId(rs.getLong(1));
                } else {
                    throw new SQLException("Saving subtask failed, no ID obtained.");
                }
            }

            return subtask;

        } catch (SQLException e) {
            throw new RuntimeException("Error saving subtask", e);
        }
    }

    @Override
    public Optional<Subtask> findById(Long subtaskId) {
        String sql = "SELECT * FROM subtasks WHERE subtask_id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, subtaskId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRowToSubtask(rs));
                }
            }

            return Optional.empty();

        } catch (SQLException e) {
            throw new RuntimeException("Error finding subtask by ID: " + subtaskId, e);
        }
    }

    @Override
    public List<Subtask> findAll() {
        String sql = "SELECT * FROM subtasks";
        List<Subtask> subtasks = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                subtasks.add(mapRowToSubtask(rs));
            }

            return subtasks;

        } catch (SQLException e) {
            throw new RuntimeException("Error finding all subtasks", e);
        }
    }

    @Override
    public List<Subtask> findByTaskId(Long taskId) {
        String sql = "SELECT * FROM subtasks WHERE task_id = ?";
        List<Subtask> subtasks = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, taskId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    subtasks.add(mapRowToSubtask(rs));
                }
            }

            return subtasks;

        } catch (SQLException e) {
            throw new RuntimeException("Error finding subtasks by task ID: " + taskId, e);
        }
    }

    @Override
    public Subtask update(Subtask subtask) {
        String sql = """
                UPDATE subtasks
                SET task_id = ?, name = ?, completed = ?
                WHERE subtask_id = ?
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, subtask.getParentTaskId());
            ps.setString(2, subtask.getName());
            ps.setBoolean(3, subtask.isCompleted());
            ps.setLong(4, subtask.getSubtaskId());

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating subtask failed, no rows affected.");
            }

            return subtask;

        } catch (SQLException e) {
            throw new RuntimeException("Error updating subtask with ID: " + subtask.getSubtaskId(), e);
        }
    }

    @Override
    public boolean deleteById(Long subtaskId) {
        String sql = "DELETE FROM subtasks WHERE subtask_id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, subtaskId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting subtask with ID: " + subtaskId, e);
        }
    }

    @Override
    public boolean existsById(Long subtaskId) {
        String sql = "SELECT 1 FROM subtasks WHERE subtask_id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, subtaskId);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error checking existence of subtask ID: " + subtaskId, e);
        }
    }

    private Subtask mapRowToSubtask(ResultSet rs) throws SQLException {
        return new Subtask(
                rs.getLong("subtask_id"),
                rs.getString("name"),
                rs.getBoolean("completed"),
                rs.getLong("task_id")
        );
    }
}