package com.demo.ordersaga.api;

public class OrderResponse {

    private final String status;
    private final String processInstanceId;
    private final String orderId;
    private final String paymentStatus;
    private final String inventoryStatus;
    private final String orderStatus;
    private final String customerId;
    private final int amount;

    public OrderResponse(
            String status,
            String processInstanceId,
            String orderId,
            String paymentStatus,
            String inventoryStatus,
            String orderStatus,
            String customerId,
            int amount
    ) {
        this.status = status;
        this.processInstanceId = processInstanceId;
        this.orderId = orderId;
        this.paymentStatus = paymentStatus;
        this.inventoryStatus = inventoryStatus;
        this.orderStatus = orderStatus;
        this.customerId = customerId;
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public String getInventoryStatus() {
        return inventoryStatus;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public String getCustomerId() {
        return customerId;
    }

    public int getAmount() {
        return amount;
    }
}
