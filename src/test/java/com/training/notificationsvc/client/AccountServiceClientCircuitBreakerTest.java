package com.training.notificationsvc.client;

import java.math.BigDecimal;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.training.notificationsvc.dto.TransactionDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

@SpringBootTest(properties = "spring.mongodb.embedded.version=4.0.12")
public class AccountServiceClientCircuitBreakerTest {

	@Autowired
	AccountServiceClient accountServiceClient;

	@RegisterExtension
	static WireMockExtension wireMockExtension = WireMockExtension.newInstance()
			.options(WireMockConfiguration.wireMockConfig().dynamicPort()).build();


	@DynamicPropertySource
	static void setup(DynamicPropertyRegistry registry){
		System.out.println(wireMockExtension.baseUrl());
		registry.add("account-service-url",()->wireMockExtension.baseUrl());
	}

	@Test
	void shouldCallFallbackWhenFailureInAccountService() {

		wireMockExtension.stubFor(WireMock.get("/api/transactions/9999").willReturn(WireMock.serviceUnavailable()));

		// 503 - Fallback

		TransactionDto failedTransactionDto = accountServiceClient.getTransaction(9999);

		Assertions.assertThat(failedTransactionDto.getAccountId()).isNull();
		Assertions.assertThat(failedTransactionDto.getBalance()).isEqualTo(BigDecimal.ZERO);
		Assertions.assertThat(failedTransactionDto.getAmount()).isEqualTo(BigDecimal.ZERO);

		// 5 request - 20%, 1 failure

		wireMockExtension.stubFor(WireMock.get("/api/transactions/9999")
				.willReturn(WireMock.aResponse()
						.withHeader("Content-type", MediaType.APPLICATION_JSON_VALUE)
						.withBodyFile("response.json")));

		for(int i =1 ; i< 5; i++){
			TransactionDto successTransactionDto = accountServiceClient.getTransaction(9999);
			Assertions.assertThat(successTransactionDto).isNotNull();
			Assertions.assertThat(successTransactionDto.getAmount()).isEqualTo(BigDecimal.valueOf(99));
			Assertions.assertThat(successTransactionDto.getTransactionId()).isEqualTo(9999);
		}

		TransactionDto successTransactionDto = accountServiceClient.getTransaction(9999);
		Assertions.assertThat(failedTransactionDto.getAccountId()).isNull();
		Assertions.assertThat(failedTransactionDto.getBalance()).isEqualTo(BigDecimal.ZERO);
		Assertions.assertThat(failedTransactionDto.getAmount()).isEqualTo(BigDecimal.ZERO);

	}
}
