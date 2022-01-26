package com.training.notificationsvc.stream;

import com.training.notificationsvc.client.AccountServiceClient;
import com.training.notificationsvc.dto.TransactionNotificationDto;
import net.bytebuddy.asm.Advice;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;

import org.mockito.Mockito;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.messaging.support.MessageBuilder;

@ExtendWith(OutputCaptureExtension.class)
public class TransactionNotificationConsumerTest {


	AccountServiceClient accountServiceClient = Mockito.mock(AccountServiceClient.class);
	TransactionNotificationConsumer consumer = new TransactionNotificationConsumer(accountServiceClient);

	@Test
	void shouldPrintMessageToConsole(CapturedOutput capturedOutput){

		TransactionNotificationDto transactionNotificationDto = new TransactionNotificationDto(123456L);
		consumer.accept(MessageBuilder.withPayload(transactionNotificationDto).build());
		Assertions.assertThat(capturedOutput.getOut()).contains("Received Transaction notification with id [123456]");

		ArgumentCaptor<Long> argumentCaptor = ArgumentCaptor.forClass(Long.class);
		Mockito.verify(accountServiceClient).getTransaction(argumentCaptor.capture());

		Assertions.assertThat(argumentCaptor.getValue()).isEqualTo(123456L);

	}
}
