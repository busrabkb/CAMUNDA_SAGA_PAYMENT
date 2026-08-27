package com.demo.ordersaga.camunda.constant;

public final class ProcessVariables {

    public static final String PROCESS_KEY = "orderSaga";
    public static final String ERROR_INVENTORY_FAILED = "InventoryFailed";

    public static final String CUSTOMER_ID = "customerId";
    public static final String AMOUNT = "amount";
    public static final String ORDER_ID = "orderId";
    public static final String PAYMENT_STATUS = "paymentStatus";
    public static final String INVENTORY_STATUS = "inventoryStatus";
    public static final String ORDER_STATUS = "orderStatus";
    public static final String COMPLETED_BY = "completedBy";
    public static final String COMPLETED_AT = "completedAt";

    public static final String CREATE_ORDER_RESPONSE = "createOrderResponse";
    public static final String PAYMENT_RESPONSE = "paymentResponse";
    public static final String INVENTORY_RESPONSE = "inventoryResponse";
    public static final String COMPLETION_RESPONSE = "completionResponse";
    public static final String REFUND_RESPONSE = "refundResponse";
    public static final String CANCEL_RESPONSE = "cancelResponse";
    public static final String FINAL_RESPONSE = "finalResponse";

    private ProcessVariables() {
    }
}
