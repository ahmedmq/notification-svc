package com.training.notificationsvc.client;

import java.math.BigDecimal;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.training.notificationsvc.dto.TransactionDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

@SpringBootTest(properties = "spring.mongodb.embedded.version=4.0.12")
@AutoConfigureWireMock(port=8090)
public class AccountServiceClientCircuitBreakerTest {

	@Autowired
	AccountServiceClient accountServiceClient;

	@Autowired
	WireMockServer wireMockServer;

//	@RegisterExtension
//	static WireMockExtension wireMockExtension = WireMockExtension.newInstance()
//			.options(WireMockConfiguration.wireMockConfig().dynamicPort()).build();


	@DynamicPropertySource
	static void setup(DynamicPropertyRegistry registry){
		registry.add("account-service-url",()->"http://localhost:8090");
	}

	@Test
	void shouldCallFallbackWhenFailureInAccountService() throws InterruptedException {

		wireMockServer.stubFor(WireMock.get("/api/transactions/9999").willReturn(WireMock.serviceUnavailable()));

		// 503 - Fallback

		TransactionDto failedTransactionDto = accountServiceClient.getTransaction(9999L);

		Assertions.assertThat(failedTransactionDto.getAccountId()).isNull();
		Assertions.assertThat(failedTransactionDto.getBalance()).isEqualTo(BigDecimal.ZERO);
		Assertions.assertThat(failedTransactionDto.getAmount()).isEqualTo(BigDecimal.ZERO);

		// 5 request - 20%, 1 failure

		wireMockServer.stubFor(WireMock.get("/api/transactions/9999")
				.willReturn(WireMock.aResponse()
						.withHeader("Content-type", MediaType.APPLICATION_JSON_VALUE)
						.withBodyFile("response.json")));

		for(int i =1 ; i< 5; i++){
			TransactionDto successTransactionDto = accountServiceClient.getTransaction(9999L);
			Assertions.assertThat(successTransactionDto).isNotNull();
			Assertions.assertThat(successTransactionDto.getAmount()).isEqualTo(BigDecimal.valueOf(99));
			Assertions.assertThat(successTransactionDto.getTransactionId()).isEqualTo(9999);
		}

		TransactionDto successTransactionDto = accountServiceClient.getTransaction(9999L);
		Assertions.assertThat(successTransactionDto.getAccountId()).isNull();
		Assertions.assertThat(successTransactionDto.getBalance()).isEqualTo(BigDecimal.ZERO);
		Assertions.assertThat(successTransactionDto.getAmount()).isEqualTo(BigDecimal.ZERO);

		Thread.sleep(5100);

		for(int i =1 ; i< 5; i++){
			TransactionDto openCircuitTransactionDto = accountServiceClient.getTransaction(9999L);
			Assertions.assertThat(openCircuitTransactionDto).isNotNull();
			Assertions.assertThat(openCircuitTransactionDto.getAmount()).isEqualTo(BigDecimal.valueOf(99));
			Assertions.assertThat(openCircuitTransactionDto.getTransactionId()).isEqualTo(9999);
		}

		TransactionDto closedCircuitTransactionDto = accountServiceClient.getTransaction(9999L);
		Assertions.assertThat(closedCircuitTransactionDto).isNotNull();
		Assertions.assertThat(closedCircuitTransactionDto.getAmount()).isEqualTo(BigDecimal.valueOf(99));
		Assertions.assertThat(closedCircuitTransactionDto.getTransactionId()).isEqualTo(9999);

	}

	@Test
	void shouldTriggerCircuitBreakerForSlowResponses() throws InterruptedException {

		// 5 sample request, Slowness Threshold = 80%, 4 of the 5 requests return a slow response
		// Mock - return a slow 4 request, Succes reponse 1 request

		wireMockServer.stubFor(WireMock.get("/api/transactions/9999")
				.willReturn(WireMock.aResponse()
						.withHeader("Content-type", MediaType.APPLICATION_JSON_VALUE)
						.withFixedDelay(2500)
						.withBodyFile("response.json")));

		for(int i =1 ; i< 5; i++){
			TransactionDto successTransactionDto = accountServiceClient.getTransaction(9999L);
			Assertions.assertThat(successTransactionDto).isNotNull();
			Assertions.assertThat(successTransactionDto.getAmount()).isEqualTo(BigDecimal.valueOf(99));
			Assertions.assertThat(successTransactionDto.getTransactionId()).isEqualTo(9999);
		}

		wireMockServer.stubFor(WireMock.get("/api/transactions/9999")
				.willReturn(WireMock.aResponse()
						.withHeader("Content-type", MediaType.APPLICATION_JSON_VALUE)
						.withBodyFile("response.json")));

		TransactionDto slowSuccessTransactionDto = accountServiceClient.getTransaction(9999L);
		Assertions.assertThat(slowSuccessTransactionDto).isNotNull();
		Assertions.assertThat(slowSuccessTransactionDto.getAmount()).isEqualTo(BigDecimal.valueOf(99));
		Assertions.assertThat(slowSuccessTransactionDto.getTransactionId()).isEqualTo(9999);

		TransactionDto successTransactionDto = accountServiceClient.getTransaction(9999L);
		Assertions.assertThat(successTransactionDto.getAccountId()).isNull();
		Assertions.assertThat(successTransactionDto.getBalance()).isEqualTo(BigDecimal.ZERO);
		Assertions.assertThat(successTransactionDto.getAmount()).isEqualTo(BigDecimal.ZERO);

		Thread.sleep(5100);

		for(int i =1 ; i< 5; i++){
			TransactionDto openCircuitTransactionDto = accountServiceClient.getTransaction(9999L);
			Assertions.assertThat(openCircuitTransactionDto).isNotNull();
			Assertions.assertThat(openCircuitTransactionDto.getAmount()).isEqualTo(BigDecimal.valueOf(99));
			Assertions.assertThat(openCircuitTransactionDto.getTransactionId()).isEqualTo(9999);
		}

		TransactionDto closedCircuitTransactionDto = accountServiceClient.getTransaction(9999L);
		Assertions.assertThat(closedCircuitTransactionDto).isNotNull();
		Assertions.assertThat(closedCircuitTransactionDto.getAmount()).isEqualTo(BigDecimal.valueOf(99));
		Assertions.assertThat(closedCircuitTransactionDto.getTransactionId()).isEqualTo(9999);

	}
}
