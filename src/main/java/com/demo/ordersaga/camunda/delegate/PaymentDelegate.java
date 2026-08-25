package com.demo.ordersaga.camunda.delegate;

import com.demo.ordersaga.camunda.ExecutionVariableReader;
import com.demo.ordersaga.camunda.constant.ProcessVariables;
import com.demo.ordersaga.domain.PaymentService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component("paymentDelegate")
public class PaymentDelegate implements JavaDelegate {

    private final PaymentService paymentService;

    public PaymentDelegate(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @Override
    public void execute(DelegateExecution execution) {
        String orderId = ExecutionVariableReader.getString(execution, ProcessVariables.ORDER_ID);
        int amount = ExecutionVariableReader.getInt(execution, ProcessVariables.AMOUNT);

        execution.setVariable(
                ProcessVariables.PAYMENT_STATUS,
                paymentService.charge(orderId, amount).name()
        );
    }
}
