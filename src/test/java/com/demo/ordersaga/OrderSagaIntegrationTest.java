package com.demo.ordersaga;

import com.demo.ordersaga.domain.model.OrderStatus;
import com.demo.ordersaga.domain.model.PaymentStatus;
import com.demo.ordersaga.infrastructure.persistence.OrderRepository;
import com.demo.ordersaga.infrastructure.persistence.PaymentRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class OrderSagaIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Test
    void createOrder_completesHappyPathAndPersistsRecords() throws Exception {
        MvcResult result = mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"customerId":"ahmet","amount":100}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.createOrder.orderId").isNotEmpty())
                .andExpect(jsonPath("$.payment.status").value("SUCCESS"))
                .andExpect(jsonPath("$.inventory.status").value("SUCCESS"))
                .andExpect(jsonPath("$.inventory.orderStatus").value("COMPLETED"))
                .andExpect(jsonPath("$.completion.completedBy").value("ahmet"))
                .andExpect(jsonPath("$.refund").isEmpty())
                .andExpect(jsonPath("$.cancellation").isEmpty())
                .andReturn();

        JsonNode body = objectMapper.readTree(result.getResponse().getContentAsString());
        String orderId = body.get("createOrder").get("orderId").asText();

        assertNotNull(orderId);
        assertEquals(OrderStatus.COMPLETED, orderRepository.findById(orderId).orElseThrow().status());
        assertEquals(PaymentStatus.SUCCESS, paymentRepository.findLatestByOrderId(orderId).orElseThrow().status());

        mockMvc.perform(get("/orders/{orderId}", orderId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("COMPLETED"))
                .andExpect(jsonPath("$.completedBy").value("ahmet"))
                .andExpect(jsonPath("$.completedAt").isNotEmpty());
    }

    @Test
    void createOrder_runsCompensationAndPersistsCancelledOrder() throws Exception {
        MvcResult result = mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"customerId":"ahmet","amount":750}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payment.status").value("SUCCESS"))
                .andExpect(jsonPath("$.inventory.status").value("FAILED"))
                .andExpect(jsonPath("$.inventory.orderStatus").value("INVENTORY_FAILED"))
                .andExpect(jsonPath("$.refund.status").value("REFUNDED"))
                .andExpect(jsonPath("$.cancellation.status").value("CANCELLED"))
                .andExpect(jsonPath("$.completion").isEmpty())
                .andReturn();

        JsonNode body = objectMapper.readTree(result.getResponse().getContentAsString());
        String orderId = body.get("createOrder").get("orderId").asText();

        assertEquals(OrderStatus.CANCELLED, orderRepository.findById(orderId).orElseThrow().status());
        assertEquals(PaymentStatus.REFUNDED, paymentRepository.findLatestByOrderId(orderId).orElseThrow().status());

        mockMvc.perform(get("/orders/{orderId}", orderId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CANCELLED"))
                .andExpect(jsonPath("$.completedBy").value((Object) null))
                .andExpect(jsonPath("$.completedAt").value((Object) null));
    }
}
