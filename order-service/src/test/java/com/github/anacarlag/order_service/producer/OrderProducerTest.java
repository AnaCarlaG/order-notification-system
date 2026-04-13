package com.github.anacarlag.order_service.producer;

import com.github.anacarlag.order_service.event.OrderCreatedEvent;
import com.github.anacarlag.order_service.model.OrderStatus;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.clients.consumer.Consumer;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.test.utils.KafkaTestUtils;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@DirtiesContext
@EmbeddedKafka(partitions = 1, topics = { "orders-topic" }, bootstrapServersProperty = "spring.kafka.bootstrap-servers")
public class OrderProducerTest {
    @Autowired
    private OrderProducer orderProducer;
    
    private final BlockingQueue<String> records = new LinkedBlockingQueue<>();

    @KafkaListener(topics = "orders-topic", groupId = "test-group")
        public void listen(String message) {
            records.add(message);
        }

        @Test
        void shouldPublishOrderCreatedEvent() throws InterruptedException {
            OrderCreatedEvent event = OrderCreatedEvent.builder()
                                    .id(1L)
                                    .customerName("Ana Carla")
                                    .productName("Notebook")
                                    .amount(new BigDecimal("4500.0"))
                                    .status(OrderStatus.PENDING)
                                    .createdAt(LocalDateTime.now())
                                    .build();
        orderProducer.sendOrderCreatedEvent(event);
        String receivedMessage = records.poll(10, TimeUnit.SECONDS);
        assertThat(receivedMessage).isNotNull();
        assertThat(receivedMessage).contains("Ana Carla");

        }
}
