package com.demo.ordersaga.api.process;

public record CreateOrderTaskResponse(String orderId, String customerId, int amount) {
}
