package com.demo.ordersaga.infrastructure.persistence;

import com.demo.ordersaga.domain.model.PaymentStatus;

import java.time.Instant;

public record PaymentRecord(
        long id,
        String orderId,
        int amount,
        PaymentStatus status,
        Instant createdAt,
        Instant updatedAt
) {
}
