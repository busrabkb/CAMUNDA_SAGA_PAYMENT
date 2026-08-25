package com.demo.ordersaga.camunda.delegate;

import com.demo.ordersaga.camunda.ExecutionVariableReader;
import com.demo.ordersaga.camunda.constant.ProcessVariables;
import com.demo.ordersaga.domain.InsufficientStockException;
import com.demo.ordersaga.domain.InventoryService;
import com.demo.ordersaga.domain.OrderService;
import com.demo.ordersaga.domain.model.InventoryStatus;
import com.demo.ordersaga.domain.model.OrderStatus;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component("inventoryDelegate")
public class InventoryDelegate implements JavaDelegate {

    private final InventoryService inventoryService;
    private final OrderService orderService;

    public InventoryDelegate(InventoryService inventoryService, OrderService orderService) {
        this.inventoryService = inventoryService;
        this.orderService = orderService;
    }

    @Override
    public void execute(DelegateExecution execution) {
        String orderId = ExecutionVariableReader.getString(execution, ProcessVariables.ORDER_ID);
        int amount = ExecutionVariableReader.getInt(execution, ProcessVariables.AMOUNT);

        try {
            var result = inventoryService.reserve(orderId, amount);
            orderService.completeOrder(orderId);
            execution.setVariable(ProcessVariables.INVENTORY_STATUS, result.inventoryStatus().name());
            execution.setVariable(ProcessVariables.ORDER_STATUS, result.orderStatus().name());
        } catch (InsufficientStockException ex) {
            orderService.markInventoryFailed(orderId);
            execution.setVariable(ProcessVariables.INVENTORY_STATUS, InventoryStatus.FAILED.name());
            execution.setVariable(ProcessVariables.ORDER_STATUS, OrderStatus.INVENTORY_FAILED.name());
            throw new BpmnError(ProcessVariables.ERROR_INVENTORY_FAILED, ex.getMessage());
        }
    }
}
