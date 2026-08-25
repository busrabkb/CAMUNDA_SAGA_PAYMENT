package com.demo.ordersaga.infrastructure.persistence;

import com.demo.ordersaga.domain.model.OrderStatus;

import java.time.Instant;

public record OrderRecord(
        String id,
        String customerId,
        int amount,
        OrderStatus status,
        String processInstanceId,
        Instant createdAt,
        Instant updatedAt
) {
}
