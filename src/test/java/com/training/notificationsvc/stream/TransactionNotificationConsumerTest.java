package com.training.notificationsvc.stream;

import com.training.notificationsvc.dto.TransactionNotificationDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;

import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.messaging.support.MessageBuilder;

@ExtendWith(OutputCaptureExtension.class)
public class TransactionNotificationConsumerTest {

	TransactionNotificationConsumer consumer = new TransactionNotificationConsumer();

	@Test
	void shouldPrintMessageToConsole(CapturedOutput capturedOutput){

		TransactionNotificationDto transactionNotificationDto = new TransactionNotificationDto(123456L);
		consumer.accept(MessageBuilder.withPayload(transactionNotificationDto).build());
		Assertions.assertThat(capturedOutput.getOut()).isEqualTo("Received Transaction notification with id [123456]");

	}
}
