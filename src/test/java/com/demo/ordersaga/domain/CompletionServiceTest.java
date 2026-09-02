package com.demo.ordersaga.domain;

import com.demo.ordersaga.infrastructure.persistence.ProcessCompletionRecord;
import com.demo.ordersaga.infrastructure.persistence.ProcessCompletionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CompletionServiceTest {

    @Mock
    private ProcessCompletionRepository processCompletionRepository;

    @InjectMocks
    private CompletionService completionService;

    @Test
    void recordCompletion_insertsWhenNotAlreadyRecorded() {
        when(processCompletionRepository.findByProcessInstanceId("proc-1")).thenReturn(Optional.empty());

        completionService.recordCompletion("order-1", "proc-1", "ahmet");

        verify(processCompletionRepository).insert("order-1", "proc-1", "ahmet");
    }

    @Test
    void recordCompletion_doesNotInsertWhenAlreadyRecorded() {
        ProcessCompletionRecord existing = new ProcessCompletionRecord(1L, "order-1", "proc-1", "ahmet", Instant.now());
        when(processCompletionRepository.findByProcessInstanceId("proc-1")).thenReturn(Optional.of(existing));

        completionService.recordCompletion("order-1", "proc-1", "ahmet");

        verify(processCompletionRepository, never()).insert(any(), any(), any());
    }

    @Test
    void findByOrderId_returnsRecordFromRepository() {
        ProcessCompletionRecord record = new ProcessCompletionRecord(1L, "order-1", "proc-1", "ahmet", Instant.now());
        when(processCompletionRepository.findByOrderId("order-1")).thenReturn(Optional.of(record));

        Optional<ProcessCompletionRecord> result = completionService.findByOrderId("order-1");

        assertTrue(result.isPresent());
        assertEquals("ahmet", result.get().completedBy());
    }
}
