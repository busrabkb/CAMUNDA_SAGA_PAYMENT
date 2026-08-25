package com.demo.ordersaga.domain;

import com.demo.ordersaga.domain.model.OrderStatus;
import com.demo.ordersaga.infrastructure.persistence.OrderRecord;
import com.demo.ordersaga.infrastructure.persistence.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    @Test
    void createOrder_persistsAndReturnsOrderId() {
        String orderId = orderService.createOrder("abc-123", "customer-1", 250);

        assertEquals("order-abc-123", orderId);
        verify(orderRepository).insert(
                eq("order-abc-123"),
                eq("customer-1"),
                eq(250),
                eq(OrderStatus.CREATED),
                eq("abc-123")
        );
    }

    @Test
    void cancelOrder_updatesStatus() {
        orderService.cancelOrder("order-abc-123");

        verify(orderRepository).updateStatus("order-abc-123", OrderStatus.CANCELLED);
    }

    @Test
    void getOrder_returnsPersistedOrder() {
        OrderRecord record = new OrderRecord(
                "order-1",
                "customer-1",
                100,
                OrderStatus.COMPLETED,
                "proc-1",
                Instant.parse("2026-01-01T00:00:00Z"),
                Instant.parse("2026-01-01T00:00:00Z")
        );
        when(orderRepository.findById("order-1")).thenReturn(Optional.of(record));

        OrderRecord result = orderService.getOrder("order-1");

        assertEquals(OrderStatus.COMPLETED, result.status());
    }
}
