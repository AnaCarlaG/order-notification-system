package com.github.anacarlag.notification_service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest
@DirtiesContext
@EmbeddedKafka(partitions = 1, topics = {"orders-topic"}, bootstrapServersProperty = "spring.kafka.bootstrap-servers")
class NotificationServiceApplicationTests {

	@Test
	void contextLoads() {
	}

}
