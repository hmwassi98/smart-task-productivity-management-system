package com.stpms.repository;

import com.stpms.model.PomodoroSession;
import com.stpms.model.PomodoroType;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcPomodoroSessionRepository implements PomodoroSessionRepository {

    private final Connection connection;

    public JdbcPomodoroSessionRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public PomodoroSession save(PomodoroSession session) {
        if (session.getStartTime() == null) {
            throw new IllegalArgumentException("Pomodoro session startTime cannot be null when saving.");
        }

        String sql = """
                INSERT INTO pomodoro_sessions (task_id, type, start_time, end_time, planned_minutes, completed)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, session.getTaskId());
            ps.setString(2, session.getType().name());
            ps.setTimestamp(3, Timestamp.valueOf(session.getStartTime()));

            if (session.getEndTime() != null) {
                ps.setTimestamp(4, Timestamp.valueOf(session.getEndTime()));
            } else {
                ps.setNull(4, Types.TIMESTAMP);
            }

            ps.setInt(5, session.getPlannedMinutes());
            ps.setBoolean(6, session.isCompleted());

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Saving pomodoro session failed, no rows affected.");
            }

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    session.setSessionId(rs.getLong(1));
                } else {
                    throw new SQLException("Saving pomodoro session failed, no ID obtained.");
                }
            }

            return session;

        } catch (SQLException e) {
            throw new RuntimeException("Error saving pomodoro session", e);
        }
    }

    @Override
    public Optional<PomodoroSession> findById(Long sessionId) {
        String sql = "SELECT * FROM pomodoro_sessions WHERE session_id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, sessionId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRowToSession(rs));
                }
            }

            return Optional.empty();

        } catch (SQLException e) {
            throw new RuntimeException("Error finding pomodoro session by ID: " + sessionId, e);
        }
    }

    @Override
    public List<PomodoroSession> findAll() {
        String sql = "SELECT * FROM pomodoro_sessions";
        List<PomodoroSession> sessions = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                sessions.add(mapRowToSession(rs));
            }

            return sessions;

        } catch (SQLException e) {
            throw new RuntimeException("Error finding all pomodoro sessions", e);
        }
    }

    @Override
    public List<PomodoroSession> findByTaskId(Long taskId) {
        String sql = "SELECT * FROM pomodoro_sessions WHERE task_id = ?";
        List<PomodoroSession> sessions = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, taskId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    sessions.add(mapRowToSession(rs));
                }
            }

            return sessions;

        } catch (SQLException e) {
            throw new RuntimeException("Error finding pomodoro sessions by task ID: " + taskId, e);
        }
    }

    @Override
    public PomodoroSession update(PomodoroSession session) {
        if (session.getStartTime() == null) {
            throw new IllegalArgumentException("Pomodoro session startTime cannot be null when updating.");
        }

        String sql = """
                UPDATE pomodoro_sessions
                SET task_id = ?, type = ?, start_time = ?, end_time = ?, planned_minutes = ?, completed = ?
                WHERE session_id = ?
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, session.getTaskId());
            ps.setString(2, session.getType().name());
            ps.setTimestamp(3, Timestamp.valueOf(session.getStartTime()));

            if (session.getEndTime() != null) {
                ps.setTimestamp(4, Timestamp.valueOf(session.getEndTime()));
            } else {
                ps.setNull(4, Types.TIMESTAMP);
            }

            ps.setInt(5, session.getPlannedMinutes());
            ps.setBoolean(6, session.isCompleted());
            ps.setLong(7, session.getSessionId());

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating pomodoro session failed, no rows affected.");
            }

            return session;

        } catch (SQLException e) {
            throw new RuntimeException("Error updating pomodoro session with ID: " + session.getSessionId(), e);
        }
    }

    @Override
    public boolean deleteById(Long sessionId) {
        String sql = "DELETE FROM pomodoro_sessions WHERE session_id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, sessionId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting pomodoro session with ID: " + sessionId, e);
        }
    }

    @Override
    public boolean existsById(Long sessionId) {
        String sql = "SELECT 1 FROM pomodoro_sessions WHERE session_id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, sessionId);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error checking existence of session ID: " + sessionId, e);
        }
    }

    private PomodoroSession mapRowToSession(ResultSet rs) throws SQLException {
        Timestamp endTimestamp = rs.getTimestamp("end_time");

        return new PomodoroSession(
                rs.getLong("session_id"),
                rs.getLong("task_id"),
                rs.getTimestamp("start_time").toLocalDateTime(),
                endTimestamp != null ? endTimestamp.toLocalDateTime() : null,
                rs.getInt("planned_minutes"),
                null,
                rs.getBoolean("completed"),
                PomodoroType.valueOf(rs.getString("type"))
        );
    }
}