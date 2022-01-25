package com.training.notificationsvc.client;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = "spring.mongodb.embedded.version=4.0.12")
public class WireMockDemoTest2 {
    
    @RegisterExtension
    static WireMockExtension wireMockExtension = WireMockExtension.newInstance()
            .options(WireMockConfiguration.wireMockConfig().dynamicPort()).build();

    @BeforeEach
    void setUp() {
        System.out.println("In Before Each..");
    }

    @AfterEach
    void tearDown() {
        System.out.println("In After Each..");
    }

    @BeforeAll
    static void beforeAll() {
        System.out.println("In Before All..");
        
    }

    @AfterAll
    static void afterAll() {
        System.out.println("In After All..");
    }

    @Test
    void name() {
        System.out.println(wireMockExtension.baseUrl());
        
        wireMockExtension.stubFor(WireMock.get("/api/transactions/123")
                .willReturn(WireMock.aResponse()
                        .withHeader("Content-type", MediaType.APPLICATION_JSON_VALUE)
                        .withFixedDelay(5000)
                        .withBody("")));
    }

    @Test
    void test2() {
    }
}
