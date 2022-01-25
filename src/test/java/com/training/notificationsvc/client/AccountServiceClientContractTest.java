package com.training.notificationsvc.client;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.training.notificationsvc.domain.TransactionType;
import com.training.notificationsvc.dto.TransactionDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = "spring.mongodb.embedded.version=4.0.12")
@AutoConfigureStubRunner(
        ids = "com.trainingdemo:account-svc:+:stubs:8082",
        stubsMode = StubRunnerProperties.StubsMode.LOCAL
)
public class AccountServiceClientContractTest {

    @Autowired
    AccountServiceClient accountServiceClient;

    @Test
    void shouldReturnTransactionDetailsForGivenId(){
        TransactionDto transactionDto = accountServiceClient.getTransaction(9999L);
        assertThat(transactionDto).isNotNull();
        assertThat(transactionDto.getAmount()).isEqualTo(BigDecimal.valueOf(99));
        assertThat(transactionDto.getBalance()).isEqualTo(BigDecimal.valueOf(100));
        assertThat(transactionDto.getTransactionId()).isEqualTo(9999);
        assertThat(transactionDto.getTransactionType()).isEqualTo(TransactionType.DEPOSIT);
        assertThat(transactionDto.getAccountId()).isEqualTo("ACC1234567");
        assertThat(transactionDto.getDescription()).isEqualTo("Token Amount");
        assertThat(transactionDto.getCreateAt()).isEqualTo("2022-01-01T01:01:01");


    }
}
