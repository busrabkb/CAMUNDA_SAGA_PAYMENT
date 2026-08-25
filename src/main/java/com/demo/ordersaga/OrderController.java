package com.demo.ordersaga;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstanceWithVariables;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
public class OrderController {

    // RuntimeService: process instance baslatmak icin Camunda API
    private final RuntimeService runtimeService;

    public OrderController(RuntimeService runtimeService) {
        this.runtimeService = runtimeService;
    }

    @PostMapping("/orders")
    public ResponseEntity<Map<String, Object>> createOrder(@RequestBody OrderRequest request) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("customerId", request.getCustomerId());
        variables.put("amount", request.getAmount());

        // Process Instance: orderSaga taniminin calisan bir ornegi
        ProcessInstanceWithVariables instance = runtimeService
                .createProcessInstanceByKey("orderSaga")
                .setVariables(variables)
                .executeWithVariablesInReturn();

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", "started");
        response.put("processInstanceId", instance.getId());
        response.put("orderId", instance.getVariables().get("orderId"));
        response.put("paymentStatus", instance.getVariables().get("paymentStatus"));
        response.put("inventoryStatus", instance.getVariables().get("inventoryStatus"));
        response.put("orderStatus", instance.getVariables().get("orderStatus"));
        response.put("customerId", request.getCustomerId());
        response.put("amount", request.getAmount());
        return ResponseEntity.ok(response);
    }
}
