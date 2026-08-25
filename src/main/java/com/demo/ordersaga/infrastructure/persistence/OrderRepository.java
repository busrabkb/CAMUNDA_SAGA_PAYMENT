package com.demo.ordersaga.infrastructure.persistence;

import com.demo.ordersaga.domain.model.OrderStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.Optional;

@Repository
public class OrderRepository {

    private static final RowMapper<OrderRecord> ROW_MAPPER = (rs, rowNum) -> new OrderRecord(
            rs.getString("id"),
            rs.getString("customer_id"),
            rs.getInt("amount"),
            OrderStatus.valueOf(rs.getString("status")),
            rs.getString("process_instance_id"),
            rs.getTimestamp("created_at").toInstant(),
            rs.getTimestamp("updated_at").toInstant()
    );

    private final JdbcTemplate jdbcTemplate;

    public OrderRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void insert(String id, String customerId, int amount, OrderStatus status, String processInstanceId) {
        jdbcTemplate.update(
                """
                INSERT INTO orders (id, customer_id, amount, status, process_instance_id)
                VALUES (?, ?, ?, ?, ?)
                """,
                id,
                customerId,
                amount,
                status.name(),
                processInstanceId
        );
    }

    public void updateStatus(String id, OrderStatus status) {
        jdbcTemplate.update(
                """
                UPDATE orders
                SET status = ?, updated_at = CURRENT_TIMESTAMP
                WHERE id = ?
                """,
                status.name(),
                id
        );
    }

    public Optional<OrderRecord> findById(String id) {
        return jdbcTemplate.query(
                """
                SELECT id, customer_id, amount, status, process_instance_id, created_at, updated_at
                FROM orders
                WHERE id = ?
                """,
                ROW_MAPPER,
                id
        ).stream().findFirst();
    }
}
