package com.demo.ordersaga.camunda;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.springframework.stereotype.Component;

import java.util.Map;

/** Reads and writes String values in Camunda process variables. */
@Component
public class CamundaVariableStore {

    public void save(DelegateExecution execution, String variableName, String value) {
        execution.setVariable(variableName, value);
    }

    public String getRequired(DelegateExecution execution, String variableName) {
        return getRequired(execution.getVariable(variableName), variableName);
    }

    public String getOptional(DelegateExecution execution, String variableName) {
        Object value = execution.getVariable(variableName);
        return value == null ? null : value.toString();
    }

    public String getRequired(Map<String, Object> variables, String variableName) {
        return getRequired(variables.get(variableName), variableName);
    }

    private String getRequired(Object value, String variableName) {
        if (value == null) {
            throw new IllegalStateException("Missing process variable: " + variableName);
        }
        return value.toString();
    }
}
