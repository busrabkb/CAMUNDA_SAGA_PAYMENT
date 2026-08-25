package com.demo.ordersaga.camunda.delegate;

import com.demo.ordersaga.camunda.ExecutionVariableReader;
import com.demo.ordersaga.camunda.constant.ProcessVariables;
import com.demo.ordersaga.domain.OrderService;
import com.demo.ordersaga.domain.model.OrderStatus;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component("cancelOrderDelegate")
public class CancelOrderDelegate implements JavaDelegate {

    private final OrderService orderService;

    public CancelOrderDelegate(OrderService orderService) {
        this.orderService = orderService;
    }

    @Override
    public void execute(DelegateExecution execution) {
        String orderId = ExecutionVariableReader.getString(execution, ProcessVariables.ORDER_ID);
        orderService.cancelOrder(orderId);
        execution.setVariable(ProcessVariables.ORDER_STATUS, OrderStatus.CANCELLED.name());
    }
}
