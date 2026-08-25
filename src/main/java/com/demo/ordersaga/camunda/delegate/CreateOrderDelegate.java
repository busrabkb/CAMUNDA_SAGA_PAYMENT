package com.demo.ordersaga.camunda.delegate;

import com.demo.ordersaga.camunda.ExecutionVariableReader;
import com.demo.ordersaga.camunda.constant.ProcessVariables;
import com.demo.ordersaga.domain.OrderService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component("createOrderDelegate")
public class CreateOrderDelegate implements JavaDelegate {

    private final OrderService orderService;

    public CreateOrderDelegate(OrderService orderService) {
        this.orderService = orderService;
    }

    @Override
    public void execute(DelegateExecution execution) {
        String customerId = ExecutionVariableReader.getString(execution, ProcessVariables.CUSTOMER_ID);
        int amount = ExecutionVariableReader.getInt(execution, ProcessVariables.AMOUNT);

        String orderId = orderService.createOrder(execution.getProcessInstanceId(), customerId, amount);
        execution.setVariable(ProcessVariables.ORDER_ID, orderId);
    }
}
