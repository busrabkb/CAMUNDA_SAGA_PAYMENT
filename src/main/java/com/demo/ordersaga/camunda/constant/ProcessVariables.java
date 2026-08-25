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

    private ProcessVariables() {
    }
}
