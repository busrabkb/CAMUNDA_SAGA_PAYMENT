package com.demo.ordersaga.domain;

import com.demo.ordersaga.domain.model.OrderStatus;
import com.demo.ordersaga.infrastructure.persistence.OrderRecord;
import com.demo.ordersaga.infrastructure.persistence.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public String createOrder(String processInstanceId, String customerId, int amount) {
        String orderId = "order-" + processInstanceId;
        orderRepository.insert(orderId, customerId, amount, OrderStatus.CREATED, processInstanceId);
        log.info("Order created. orderId={}, customerId={}, amount={}", orderId, customerId, amount);
        return orderId;
    }

    public void completeOrder(String orderId) {
        orderRepository.updateStatus(orderId, OrderStatus.COMPLETED);
        log.info("Order completed. orderId={}", orderId);
    }

    public void markInventoryFailed(String orderId) {
        orderRepository.updateStatus(orderId, OrderStatus.INVENTORY_FAILED);
        log.info("Order inventory failed. orderId={}", orderId);
    }

    public void cancelOrder(String orderId) {
        orderRepository.updateStatus(orderId, OrderStatus.CANCELLED);
        log.info("Order cancelled. orderId={}", orderId);
    }

    public OrderRecord getOrder(String orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));
    }
}
