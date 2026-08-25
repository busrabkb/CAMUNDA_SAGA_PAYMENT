package com.demo.ordersaga.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderService.class);

    public String createOrder(String processInstanceId, String customerId, int amount) {
        String orderId = "order-" + processInstanceId;
        log.info("Order created. orderId={}, customerId={}, amount={}", orderId, customerId, amount);
        return orderId;
    }

    public void cancelOrder(String orderId) {
        log.info("Order cancelled. orderId={}", orderId);
    }
}
