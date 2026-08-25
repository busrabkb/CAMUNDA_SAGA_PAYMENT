package com.demo.ordersaga.domain;

import com.demo.ordersaga.domain.model.PaymentStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PaymentServiceTest {

    private final PaymentService paymentService = new PaymentService();

    @Test
    void charge_returnsSuccess() {
        assertEquals(PaymentStatus.SUCCESS, paymentService.charge("order-1", 100));
    }

    @Test
    void refund_returnsRefunded() {
        assertEquals(PaymentStatus.REFUNDED, paymentService.refund("order-1", 100));
    }
}
