package com.demo.ordersaga.api;

import com.demo.ordersaga.api.process.CancelOrderTaskResponse;
import com.demo.ordersaga.api.process.CompletionTaskResponse;
import com.demo.ordersaga.api.process.CreateOrderTaskResponse;
import com.demo.ordersaga.api.process.InventoryTaskResponse;
import com.demo.ordersaga.api.process.PaymentTaskResponse;
import com.demo.ordersaga.api.process.RefundPaymentTaskResponse;

/** The final API DTO assembled by the last BPMN service task. */
public record OrderResponse(
        String processInstanceId,
        CreateOrderTaskResponse createOrder,
        PaymentTaskResponse payment,
        InventoryTaskResponse inventory,
        CompletionTaskResponse completion,
        RefundPaymentTaskResponse refund,
        CancelOrderTaskResponse cancellation
) {
}
