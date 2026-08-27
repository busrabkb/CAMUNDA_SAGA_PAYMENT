package com.demo.ordersaga.api.process;

public record InventoryTaskResponse(String status, String orderStatus, String failureReason) {
}
