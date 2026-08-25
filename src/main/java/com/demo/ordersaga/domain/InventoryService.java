package com.demo.ordersaga.domain;

import com.demo.ordersaga.domain.model.InventoryResult;
import com.demo.ordersaga.domain.model.InventoryStatus;
import com.demo.ordersaga.domain.model.OrderStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class InventoryService {

    private static final int STOCK_LIMIT = 500;

    private static final Logger log = LoggerFactory.getLogger(InventoryService.class);

    public InventoryResult reserve(String orderId, int amount) {
        if (amount > STOCK_LIMIT) {
            log.info("Inventory failed. orderId={}, amount={}", orderId, amount);
            throw new InsufficientStockException("Not enough stock");
        }

        log.info("Inventory reserved. orderId={}", orderId);
        return new InventoryResult(InventoryStatus.SUCCESS, OrderStatus.COMPLETED);
    }
}
