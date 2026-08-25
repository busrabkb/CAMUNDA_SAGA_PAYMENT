package com.demo.ordersaga.api;

import com.demo.ordersaga.domain.model.OrderStatus;
import com.demo.ordersaga.infrastructure.persistence.OrderRecord;
import com.demo.ordersaga.infrastructure.persistence.ProcessCompletionRecord;

import java.time.Instant;
import java.util.Optional;

public class OrderDetailResponse {

    private final String orderId;
    private final String customerId;
    private final int amount;
    private final OrderStatus status;
    private final String processInstanceId;
    private final Instant createdAt;
    private final Instant updatedAt;
    private final String completedBy;
    private final Instant completedAt;

    public OrderDetailResponse(OrderRecord order, Optional<ProcessCompletionRecord> completion) {
        this.orderId = order.id();
        this.customerId = order.customerId();
        this.amount = order.amount();
        this.status = order.status();
        this.processInstanceId = order.processInstanceId();
        this.createdAt = order.createdAt();
        this.updatedAt = order.updatedAt();
        this.completedBy = completion.map(ProcessCompletionRecord::completedBy).orElse(null);
        this.completedAt = completion.map(ProcessCompletionRecord::completedAt).orElse(null);
    }

    public String getOrderId() {
        return orderId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public int getAmount() {
        return amount;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public String getCompletedBy() {
        return completedBy;
    }

    public Instant getCompletedAt() {
        return completedAt;
    }
}
