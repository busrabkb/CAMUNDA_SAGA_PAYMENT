package com.demo.ordersaga.domain;

import com.demo.ordersaga.domain.model.PaymentStatus;
import com.demo.ordersaga.infrastructure.persistence.PaymentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private PaymentService paymentService;

    @Test
    void charge_persistsSuccessPayment() {
        PaymentStatus status = paymentService.charge("order-1", 100);

        assertEquals(PaymentStatus.SUCCESS, status);
        verify(paymentRepository).insert("order-1", 100, PaymentStatus.SUCCESS);
    }

    @Test
    void refund_updatesLatestPaymentStatus() {
        PaymentStatus status = paymentService.refund("order-1", 100);

        assertEquals(PaymentStatus.REFUNDED, status);
        verify(paymentRepository).updateLatestStatusByOrderId(eq("order-1"), eq(PaymentStatus.REFUNDED));
    }
}
