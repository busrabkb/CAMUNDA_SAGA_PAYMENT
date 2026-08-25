package com.demo.ordersaga;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PaymentDelegate implements JavaDelegate {

    private static final Logger log = LoggerFactory.getLogger(PaymentDelegate.class);

    @Override
    public void execute(DelegateExecution execution) {
        String orderId = (String) execution.getVariable("orderId");
        Object amount = execution.getVariable("amount");

        execution.setVariable("paymentStatus", "SUCCESS");
        log.info("Payment successful. orderId={}, amount={}", orderId, amount);
    }
}
