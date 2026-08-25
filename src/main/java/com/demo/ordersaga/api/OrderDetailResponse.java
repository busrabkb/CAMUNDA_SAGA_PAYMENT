package com.demo.ordersaga.api;

import com.demo.ordersaga.domain.model.OrderStatus;
import com.demo.ordersaga.infrastructure.persistence.OrderRecord;

import java.time.Instant;

public class OrderDetailResponse {

    private final String orderId;
    private final String customerId;
    private final int amount;
    private final OrderStatus status;
    private final String processInstanceId;
    private final Instant createdAt;
    private final Instant updatedAt;

    public OrderDetailResponse(OrderRecord order) {
        this.orderId = order.id();
        this.customerId = order.customerId();
        this.amount = order.amount();
        this.status = order.status();
        this.processInstanceId = order.processInstanceId();
        this.createdAt = order.createdAt();
        this.updatedAt = order.updatedAt();
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
}
