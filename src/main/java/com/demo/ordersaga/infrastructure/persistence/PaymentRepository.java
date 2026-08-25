package com.demo.ordersaga.infrastructure.persistence;

import com.demo.ordersaga.domain.model.PaymentStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class PaymentRepository {

    private static final RowMapper<PaymentRecord> ROW_MAPPER = (rs, rowNum) -> new PaymentRecord(
            rs.getLong("id"),
            rs.getString("order_id"),
            rs.getInt("amount"),
            PaymentStatus.valueOf(rs.getString("status")),
            rs.getTimestamp("created_at").toInstant(),
            rs.getTimestamp("updated_at").toInstant()
    );

    private final JdbcTemplate jdbcTemplate;

    public PaymentRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void insert(String orderId, int amount, PaymentStatus status) {
        jdbcTemplate.update(
                """
                INSERT INTO payments (order_id, amount, status)
                VALUES (?, ?, ?)
                """,
                orderId,
                amount,
                status.name()
        );
    }

    public void updateLatestStatusByOrderId(String orderId, PaymentStatus status) {
        jdbcTemplate.update(
                """
                UPDATE payments
                SET status = ?, updated_at = CURRENT_TIMESTAMP
                WHERE order_id = ?
                  AND id = (SELECT MAX(id) FROM payments WHERE order_id = ?)
                """,
                status.name(),
                orderId,
                orderId
        );
    }

    public Optional<PaymentRecord> findLatestByOrderId(String orderId) {
        return jdbcTemplate.query(
                """
                SELECT id, order_id, amount, status, created_at, updated_at
                FROM payments
                WHERE order_id = ?
                ORDER BY id DESC
                LIMIT 1
                """,
                ROW_MAPPER,
                orderId
        ).stream().findFirst();
    }
}
