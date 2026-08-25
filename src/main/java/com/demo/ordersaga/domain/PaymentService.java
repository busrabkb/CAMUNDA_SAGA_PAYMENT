package com.demo.ordersaga.domain;

import com.demo.ordersaga.domain.model.PaymentStatus;
import com.demo.ordersaga.infrastructure.persistence.PaymentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    private static final Logger log = LoggerFactory.getLogger(PaymentService.class);

    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public PaymentStatus charge(String orderId, int amount) {
        paymentRepository.insert(orderId, amount, PaymentStatus.SUCCESS);
        log.info("Payment successful. orderId={}, amount={}", orderId, amount);
        return PaymentStatus.SUCCESS;
    }

    public PaymentStatus refund(String orderId, int amount) {
        paymentRepository.updateLatestStatusByOrderId(orderId, PaymentStatus.REFUNDED);
        log.info("Payment refunded. orderId={}, amount={}", orderId, amount);
        return PaymentStatus.REFUNDED;
    }
}
