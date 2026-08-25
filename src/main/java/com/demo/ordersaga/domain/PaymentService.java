package com.demo.ordersaga.domain;

import com.demo.ordersaga.domain.model.PaymentStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    private static final Logger log = LoggerFactory.getLogger(PaymentService.class);

    public PaymentStatus charge(String orderId, int amount) {
        log.info("Payment successful. orderId={}, amount={}", orderId, amount);
        return PaymentStatus.SUCCESS;
    }

    public PaymentStatus refund(String orderId, int amount) {
        log.info("Payment refunded. orderId={}, amount={}", orderId, amount);
        return PaymentStatus.REFUNDED;
    }
}
