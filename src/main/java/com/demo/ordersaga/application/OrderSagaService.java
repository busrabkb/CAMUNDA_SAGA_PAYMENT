package com.demo.ordersaga.application;

import com.demo.ordersaga.api.OrderRequest;
import com.demo.ordersaga.api.OrderResponse;
import com.demo.ordersaga.camunda.CamundaVariableStore;
import com.demo.ordersaga.camunda.TaskResponseJsonMapper;
import com.demo.ordersaga.camunda.constant.ProcessVariables;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstanceWithVariables;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class OrderSagaService {

    private final RuntimeService runtimeService;
    private final CamundaVariableStore variableStore;
    private final TaskResponseJsonMapper responseJsonMapper;

    public OrderSagaService(
            RuntimeService runtimeService,
            CamundaVariableStore variableStore,
            TaskResponseJsonMapper responseJsonMapper
    ) {
        this.runtimeService = runtimeService;
        this.variableStore = variableStore;
        this.responseJsonMapper = responseJsonMapper;
    }

    public OrderResponse startOrder(OrderRequest request) {
        Map<String, Object> variables = new HashMap<>();
        variables.put(ProcessVariables.CUSTOMER_ID, request.getCustomerId());
        variables.put(ProcessVariables.AMOUNT, request.getAmount());

        ProcessInstanceWithVariables instance = runtimeService
                .createProcessInstanceByKey(ProcessVariables.PROCESS_KEY)
                .setVariables(variables)
                .executeWithVariablesInReturn();

        String finalResponseJson = variableStore.getRequired(instance.getVariables(), ProcessVariables.FINAL_RESPONSE);
        return responseJsonMapper.fromJson(finalResponseJson, OrderResponse.class);
    }
}
