package com.demo.ordersaga.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OrderServiceTest {

    private final OrderService orderService = new OrderService();

    @Test
    void createOrder_generatesOrderIdFromProcessInstanceId() {
        String orderId = orderService.createOrder("abc-123", "customer-1", 250);

        assertEquals("order-abc-123", orderId);
    }

    @Test
    void cancelOrder_doesNotThrow() {
        orderService.cancelOrder("order-abc-123");
    }
}
