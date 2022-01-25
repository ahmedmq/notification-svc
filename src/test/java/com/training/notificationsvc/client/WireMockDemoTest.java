package com.training.notificationsvc.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.training.notificationsvc.domain.TransactionType;
import com.training.notificationsvc.dto.TransactionDto;
import com.training.notificationsvc.dto.TransactionNotificationDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@WireMockTest
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = "spring.mongodb.embedded.version=4.0.12")
public class WireMockDemoTest {

    @Autowired
    TestRestTemplate testRestTemplate;

    @Test
    void testWireMockServer(WireMockRuntimeInfo wireMockRuntimeInfo) throws JsonProcessingException {

        TransactionDto transactionDto = new TransactionDto(1L, TransactionType.DEPOSIT,"ACC1234567",BigDecimal.valueOf(99),BigDecimal.valueOf(100),"Token Amount","2022-01-01T01:01:01");

        ObjectMapper objectMapper = new ObjectMapper();

        String json = objectMapper.writeValueAsString(transactionDto);
//
//        WireMock.stubFor(WireMock.get("/api/transactions/123")
//                .willReturn(WireMock.aResponse()
//                        .withHeader("Content-type", MediaType.APPLICATION_JSON_VALUE)
//                        .withBody(json)));

        WireMock.stubFor(WireMock.get("/api/transactions/123")
                .willReturn(WireMock.aResponse()
                        .withHeader("Content-type", MediaType.APPLICATION_JSON_VALUE)
                        .withBodyFile("response.json")));

        // TestRestTemplate
        // wiremock server:port + /api/transactions/123
        String url = wireMockRuntimeInfo.getHttpBaseUrl()+"/api/transactions/123";

        ResponseEntity<TransactionDto> responseEntity = testRestTemplate.exchange(url, HttpMethod.GET, null, TransactionDto.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody().getTransactionId()).isEqualTo(9999);
        assertThat(responseEntity.getBody().getAmount()).isEqualTo(BigDecimal.valueOf(99));



    }


}
