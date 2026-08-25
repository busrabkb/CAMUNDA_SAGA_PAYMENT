package com.demo.ordersaga.api;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class OrderRequest {

    @NotBlank
    private String customerId;

    @Min(1)
    private int amount;

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
