package com.demo.ordersaga.infrastructure.persistence;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.Optional;

@Repository
public class ProcessCompletionRepository {

    private static final RowMapper<ProcessCompletionRecord> ROW_MAPPER = (rs, rowNum) -> new ProcessCompletionRecord(
            rs.getLong("id"),
            rs.getString("order_id"),
            rs.getString("process_instance_id"),
            rs.getString("completed_by"),
            rs.getTimestamp("completed_at").toInstant()
    );

    private final JdbcTemplate jdbcTemplate;

    public ProcessCompletionRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void insert(String orderId, String processInstanceId, String completedBy) {
        jdbcTemplate.update(
                """
                INSERT INTO process_completions (order_id, process_instance_id, completed_by)
                VALUES (?, ?, ?)
                """,
                orderId,
                processInstanceId,
                completedBy
        );
    }

    public Optional<ProcessCompletionRecord> findByProcessInstanceId(String processInstanceId) {
        return jdbcTemplate.query(
                """
                SELECT id, order_id, process_instance_id, completed_by, completed_at
                FROM process_completions
                WHERE process_instance_id = ?
                """,
                ROW_MAPPER,
                processInstanceId
        ).stream().findFirst();
    }

    public Optional<ProcessCompletionRecord> findByOrderId(String orderId) {
        return jdbcTemplate.query(
                """
                SELECT id, order_id, process_instance_id, completed_by, completed_at
                FROM process_completions
                WHERE order_id = ?
                """,
                ROW_MAPPER,
                orderId
        ).stream().findFirst();
    }
}
