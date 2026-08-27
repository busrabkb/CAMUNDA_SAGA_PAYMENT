package com.demo.ordersaga.camunda.delegate;

import com.demo.ordersaga.api.process.CreateOrderTaskResponse;
import com.demo.ordersaga.camunda.ExecutionVariableReader;
import com.demo.ordersaga.camunda.CamundaVariableStore;
import com.demo.ordersaga.camunda.TaskResponseJsonMapper;
import com.demo.ordersaga.camunda.constant.ProcessVariables;
import com.demo.ordersaga.domain.OrderService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component("createOrderDelegate")
public class CreateOrderDelegate implements JavaDelegate {

    private final OrderService orderService;
    private final CamundaVariableStore variableStore;
    private final TaskResponseJsonMapper responseJsonMapper;

    public CreateOrderDelegate(OrderService orderService, CamundaVariableStore variableStore, TaskResponseJsonMapper responseJsonMapper) {
        this.orderService = orderService;
        this.variableStore = variableStore;
        this.responseJsonMapper = responseJsonMapper;
    }

    @Override
    public void execute(DelegateExecution execution) {
        String customerId = ExecutionVariableReader.getString(execution, ProcessVariables.CUSTOMER_ID);
        int amount = ExecutionVariableReader.getInt(execution, ProcessVariables.AMOUNT);

        String orderId = orderService.createOrder(execution.getProcessInstanceId(), customerId, amount);
        execution.setVariable(ProcessVariables.ORDER_ID, orderId);
        variableStore.save(execution, ProcessVariables.CREATE_ORDER_RESPONSE,
                responseJsonMapper.toJson(new CreateOrderTaskResponse(orderId, customerId, amount)));
    }
}
