package com.demo.ordersaga.camunda;

import org.camunda.bpm.engine.delegate.DelegateExecution;

public final class ExecutionVariableReader {

    private ExecutionVariableReader() {
    }

    public static String getString(DelegateExecution execution, String name) {
        return (String) execution.getVariable(name);
    }

    public static int getInt(DelegateExecution execution, String name) {
        Object value = execution.getVariable(name);
        if (value instanceof Number number) {
            return number.intValue();
        }
        return 0;
    }
}
