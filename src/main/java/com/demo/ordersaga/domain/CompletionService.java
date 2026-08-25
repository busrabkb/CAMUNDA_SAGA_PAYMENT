package com.demo.ordersaga.domain;

import com.demo.ordersaga.infrastructure.persistence.ProcessCompletionRecord;
import com.demo.ordersaga.infrastructure.persistence.ProcessCompletionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CompletionService {

    private static final Logger log = LoggerFactory.getLogger(CompletionService.class);

    private final ProcessCompletionRepository processCompletionRepository;

    public CompletionService(ProcessCompletionRepository processCompletionRepository) {
        this.processCompletionRepository = processCompletionRepository;
    }

    /**
     * Records who completed the process. completedBy comes from the POST /orders endpoint (customerId).
     */
    public void recordCompletion(String orderId, String processInstanceId, String completedBy) {
        if (processCompletionRepository.findByProcessInstanceId(processInstanceId).isPresent()) {
            log.info("Completion already recorded. processInstanceId={}", processInstanceId);
            return;
        }

        processCompletionRepository.insert(orderId, processInstanceId, completedBy);
        log.info("Process completion recorded. orderId={}, completedBy={}", orderId, completedBy);
    }

    public Optional<ProcessCompletionRecord> findByOrderId(String orderId) {
        return processCompletionRepository.findByOrderId(orderId);
    }
}
