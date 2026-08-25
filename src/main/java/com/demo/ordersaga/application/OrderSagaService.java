package com.demo.ordersaga.application;

import com.demo.ordersaga.api.OrderRequest;
import com.demo.ordersaga.api.OrderResponse;
import com.demo.ordersaga.camunda.constant.ProcessVariables;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstanceWithVariables;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class OrderSagaService {

    private final RuntimeService runtimeService;

    public OrderSagaService(RuntimeService runtimeService) {
        this.runtimeService = runtimeService;
    }

    public OrderResponse startOrder(OrderRequest request) {
        Map<String, Object> variables = new HashMap<>();
        variables.put(ProcessVariables.CUSTOMER_ID, request.getCustomerId());
        variables.put(ProcessVariables.AMOUNT, request.getAmount());

        ProcessInstanceWithVariables instance = runtimeService
                .createProcessInstanceByKey(ProcessVariables.PROCESS_KEY)
                .setVariables(variables)
                .executeWithVariablesInReturn();

        Map<String, Object> processVariables = instance.getVariables();
        return new OrderResponse(
                "started",
                instance.getId(),
                (String) processVariables.get(ProcessVariables.ORDER_ID),
                (String) processVariables.get(ProcessVariables.PAYMENT_STATUS),
                (String) processVariables.get(ProcessVariables.INVENTORY_STATUS),
                (String) processVariables.get(ProcessVariables.ORDER_STATUS),
                request.getCustomerId(),
                request.getAmount()
        );
    }
}
