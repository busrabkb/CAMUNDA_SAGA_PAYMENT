package com.demo.ordersaga.camunda.delegate;

import com.demo.ordersaga.api.process.PaymentTaskResponse;
import com.demo.ordersaga.camunda.ExecutionVariableReader;
import com.demo.ordersaga.camunda.CamundaVariableStore;
import com.demo.ordersaga.camunda.TaskResponseJsonMapper;
import com.demo.ordersaga.camunda.constant.ProcessVariables;
import com.demo.ordersaga.domain.PaymentService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component("paymentDelegate")
public class PaymentDelegate implements JavaDelegate {

    private final PaymentService paymentService;
    private final CamundaVariableStore variableStore;
    private final TaskResponseJsonMapper responseJsonMapper;

    public PaymentDelegate(PaymentService paymentService, CamundaVariableStore variableStore, TaskResponseJsonMapper responseJsonMapper) {
        this.paymentService = paymentService;
        this.variableStore = variableStore;
        this.responseJsonMapper = responseJsonMapper;
    }

    @Override
    public void execute(DelegateExecution execution) {
        String orderId = ExecutionVariableReader.getString(execution, ProcessVariables.ORDER_ID);
        int amount = ExecutionVariableReader.getInt(execution, ProcessVariables.AMOUNT);

        String status = paymentService.charge(orderId, amount).name();
        execution.setVariable(ProcessVariables.PAYMENT_STATUS, status);
        variableStore.save(execution, ProcessVariables.PAYMENT_RESPONSE,
                responseJsonMapper.toJson(new PaymentTaskResponse(status)));
    }
}
