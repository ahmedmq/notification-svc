package com.training.notificationsvc.integration;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.training.notificationsvc.dto.TransactionNotificationDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.http.MediaType;
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

    @RegisterExtension
    static WireMockExtension wireMockExtension = WireMockExtension.newInstance()
            .options(WireMockConfiguration.wireMockConfig().dynamicPort()).build();


    @Autowired
    StreamBridge streamBridge;

    @DynamicPropertySource
    static void setup(DynamicPropertyRegistry registry){
        registry.add("spring.rabbitmq.host",()->rabbitMQContainer.getHost());
        registry.add("spring.rabbitmq.port",()->rabbitMQContainer.getAmqpPort());
        registry.add("spring.data.mongodb.uri",()->mongoDBContainer.getReplicaSetUrl());
        registry.add("account-service-url",()->wireMockExtension.baseUrl());
    }

    @Test
    void shouldConsumerMessage() throws InterruptedException {

        wireMockExtension.stubFor(WireMock.get("/api/transactions/9999")
                .willReturn(WireMock.aResponse()
                        .withHeader("Content-type", MediaType.APPLICATION_JSON_VALUE)
                        .withBodyFile("response.json")));

        //Arrange
        Message notification = MessageBuilder.withPayload(new TransactionNotificationDto(9999L)).build();
        streamBridge.send("transaction-notification",notification);
        Thread.sleep(10000);


    }

}
