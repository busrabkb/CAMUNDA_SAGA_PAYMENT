package com.demo.ordersaga.api;

import com.demo.ordersaga.application.OrderSagaService;
import com.demo.ordersaga.domain.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {

    private final OrderSagaService orderSagaService;
    private final OrderService orderService;

    public OrderController(OrderSagaService orderSagaService, OrderService orderService) {
        this.orderSagaService = orderSagaService;
        this.orderService = orderService;
    }

    @PostMapping("/orders")
    public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody OrderRequest request) {
        return ResponseEntity.ok(orderSagaService.startOrder(request));
    }

    @GetMapping("/orders/{orderId}")
    public ResponseEntity<OrderDetailResponse> getOrder(@PathVariable String orderId) {
        return ResponseEntity.ok(new OrderDetailResponse(orderService.getOrder(orderId)));
    }
}
