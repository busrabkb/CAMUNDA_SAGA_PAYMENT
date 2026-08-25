package com.demo.ordersaga.infrastructure.persistence;

import java.time.Instant;

public record ProcessCompletionRecord(
        long id,
        String orderId,
        String processInstanceId,
        String completedBy,
        Instant completedAt
) {
}
