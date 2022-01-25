package com.training.notificationsvc.client;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.training.notificationsvc.dto.TransactionDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.math.BigDecimal;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = "spring.mongodb.embedded.version=4.0.12")
public class AccountServiceClientTest {

    @RegisterExtension
    static WireMockExtension wireMockExtension = WireMockExtension.newInstance()
            .options(WireMockConfiguration.wireMockConfig().dynamicPort()).build();

    @Autowired
    AccountServiceClient accountServiceClient;

    @DynamicPropertySource
    static void setup(DynamicPropertyRegistry registry){
        System.out.println(wireMockExtension.baseUrl());
       registry.add("account-service-url",()->wireMockExtension.baseUrl());
    }

    @Test
    void shouldReturnTransactionDetailsForGivenId(){
        wireMockExtension.stubFor(WireMock.get("/api/transactions/9999")
                .willReturn(WireMock.aResponse()
                        .withHeader("Content-type", MediaType.APPLICATION_JSON_VALUE)
                        .withBodyFile("response.json")));

        TransactionDto transactionDto = accountServiceClient.getTransaction(9999L);
        Assertions.assertThat(transactionDto).isNotNull();
        Assertions.assertThat(transactionDto.getAmount()).isEqualTo(BigDecimal.valueOf(99));
        Assertions.assertThat(transactionDto.getTransactionId()).isEqualTo(9999);
    }
}
