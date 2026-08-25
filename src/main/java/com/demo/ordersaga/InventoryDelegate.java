package com.demo.ordersaga;

import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InventoryDelegate implements JavaDelegate {

    private static final Logger log = LoggerFactory.getLogger(InventoryDelegate.class);

    @Override
    public void execute(DelegateExecution execution) {
        String orderId = (String) execution.getVariable("orderId");
        int amount = toInt(execution.getVariable("amount"));

        if (amount > 500) {
            execution.setVariable("inventoryStatus", "FAILED");
            execution.setVariable("orderStatus", "INVENTORY_FAILED");
            log.info("Inventory failed. orderId={}, amount={}", orderId, amount);
            throw new BpmnError("InventoryFailed", "Not enough stock");
        }

        execution.setVariable("inventoryStatus", "SUCCESS");
        execution.setVariable("orderStatus", "COMPLETED");
        log.info("Inventory reserved. orderId={}", orderId);
    }

    private int toInt(Object value) {
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        return 0;
    }
}
