package com.demo.ordersaga.domain;

import com.demo.ordersaga.domain.model.InventoryStatus;
import com.demo.ordersaga.domain.model.OrderStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class InventoryServiceTest {

    private final InventoryService inventoryService = new InventoryService();

    @Test
    void reserve_succeedsWhenAmountWithinLimit() {
        var result = inventoryService.reserve("order-1", 500);

        assertEquals(InventoryStatus.SUCCESS, result.inventoryStatus());
        assertEquals(OrderStatus.COMPLETED, result.orderStatus());
    }

    @Test
    void reserve_failsWhenAmountExceedsLimit() {
        InsufficientStockException exception = assertThrows(
                InsufficientStockException.class,
                () -> inventoryService.reserve("order-1", 501)
        );

        assertEquals("Not enough stock", exception.getMessage());
    }
}
