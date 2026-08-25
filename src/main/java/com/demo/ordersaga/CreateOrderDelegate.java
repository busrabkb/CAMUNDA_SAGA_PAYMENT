package com.demo.ordersaga;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// JavaDelegate: BPMN service task camunda:class ile bu sinifi cagirir
public class CreateOrderDelegate implements JavaDelegate {

    private static final Logger log = LoggerFactory.getLogger(CreateOrderDelegate.class);

    @Override
    public void execute(DelegateExecution execution) {
        String customerId = (String) execution.getVariable("customerId");
        Object amount = execution.getVariable("amount");

        String orderId = "order-" + execution.getProcessInstanceId();
        execution.setVariable("orderId", orderId);

        log.info("Order created. orderId={}, customerId={}, amount={}", orderId, customerId, amount);
    }
}
