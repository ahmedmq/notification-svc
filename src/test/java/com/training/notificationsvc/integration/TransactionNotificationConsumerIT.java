package com.training.notificationsvc.integration;

import com.training.notificationsvc.dto.TransactionNotificationDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@SpringBootTest
@EnableAutoConfiguration(exclude = EmbeddedMongoAutoConfiguration.class)
public class TransactionNotificationConsumerIT {

    @Container
    static RabbitMQContainer rabbitMQContainer = new RabbitMQContainer("rabbitmq:3-management");

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo");

    @Autowired
    StreamBridge streamBridge;

    @DynamicPropertySource
    static void setup(DynamicPropertyRegistry registry){
        registry.add("spring.rabbitmq.host",()->rabbitMQContainer.getHost());
        registry.add("spring.rabbitmq.port",()->rabbitMQContainer.getAmqpPort());
        registry.add("spring.data.mongodb.uri",()->mongoDBContainer.getReplicaSetUrl());
    }

    @Test
    void shouldConsumerMessage() throws InterruptedException {
        //Arrange
        Message notification = MessageBuilder.withPayload(new TransactionNotificationDto(1L)).build();
        streamBridge.send("transaction-notification",notification);
        Thread.sleep(10000);
    }

}
