package com.demo.ordersaga.camunda.delegate;

import com.demo.ordersaga.api.OrderResponse;
import com.demo.ordersaga.api.process.CancelOrderTaskResponse;
import com.demo.ordersaga.api.process.CompletionTaskResponse;
import com.demo.ordersaga.api.process.CreateOrderTaskResponse;
import com.demo.ordersaga.api.process.InventoryTaskResponse;
import com.demo.ordersaga.api.process.PaymentTaskResponse;
import com.demo.ordersaga.api.process.RefundPaymentTaskResponse;
import com.demo.ordersaga.camunda.CamundaVariableStore;
import com.demo.ordersaga.camunda.TaskResponseJsonMapper;
import com.demo.ordersaga.camunda.constant.ProcessVariables;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component("buildOrderResponseDelegate")
public class BuildOrderResponseDelegate implements JavaDelegate {

    private final CamundaVariableStore variableStore;
    private final TaskResponseJsonMapper responseJsonMapper;

    public BuildOrderResponseDelegate(CamundaVariableStore variableStore, TaskResponseJsonMapper responseJsonMapper) {
        this.variableStore = variableStore;
        this.responseJsonMapper = responseJsonMapper;
    }

    @Override
    public void execute(DelegateExecution execution) {
        OrderResponse response = new OrderResponse(
                execution.getProcessInstanceId(),
                responseJsonMapper.fromJson(variableStore.getRequired(execution, ProcessVariables.CREATE_ORDER_RESPONSE), CreateOrderTaskResponse.class),
                responseJsonMapper.fromJson(variableStore.getRequired(execution, ProcessVariables.PAYMENT_RESPONSE), PaymentTaskResponse.class),
                responseJsonMapper.fromJson(variableStore.getRequired(execution, ProcessVariables.INVENTORY_RESPONSE), InventoryTaskResponse.class),
                readOptional(execution, ProcessVariables.COMPLETION_RESPONSE, CompletionTaskResponse.class),
                readOptional(execution, ProcessVariables.REFUND_RESPONSE, RefundPaymentTaskResponse.class),
                readOptional(execution, ProcessVariables.CANCEL_RESPONSE, CancelOrderTaskResponse.class)
        );

        variableStore.save(execution, ProcessVariables.FINAL_RESPONSE, responseJsonMapper.toJson(response));
    }

    private <T> T readOptional(DelegateExecution execution, String variableName, Class<T> responseType) {
        String json = variableStore.getOptional(execution, variableName);
        return json == null ? null : responseJsonMapper.fromJson(json, responseType);
    }
}
