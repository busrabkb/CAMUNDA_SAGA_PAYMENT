package com.demo.ordersaga.camunda.delegate;

import com.demo.ordersaga.camunda.ExecutionVariableReader;
import com.demo.ordersaga.camunda.constant.ProcessVariables;
import com.demo.ordersaga.domain.CompletionService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component("recordCompletionDelegate")
public class RecordCompletionDelegate implements JavaDelegate {

    private final CompletionService completionService;

    public RecordCompletionDelegate(CompletionService completionService) {
        this.completionService = completionService;
    }

    @Override
    public void execute(DelegateExecution execution) {
        String orderId = ExecutionVariableReader.getString(execution, ProcessVariables.ORDER_ID);
        String completedBy = ExecutionVariableReader.getString(execution, ProcessVariables.CUSTOMER_ID);
        String processInstanceId = execution.getProcessInstanceId();
        Instant completedAt = Instant.now();

        completionService.recordCompletion(orderId, processInstanceId, completedBy);
        execution.setVariable(ProcessVariables.COMPLETED_BY, completedBy);
        execution.setVariable(ProcessVariables.COMPLETED_AT, completedAt.toString());
    }
}
