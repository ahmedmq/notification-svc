package com.training.notificationsvc.stream;

import java.util.function.Consumer;

import com.training.notificationsvc.client.AccountServiceClient;
import com.training.notificationsvc.dto.TransactionDto;
import com.training.notificationsvc.dto.TransactionNotificationDto;

import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Component
public class TransactionNotificationConsumer implements Consumer<Message<TransactionNotificationDto>> {

	private final AccountServiceClient accountServiceClient;

	public TransactionNotificationConsumer(AccountServiceClient accountServiceClient) {
		this.accountServiceClient = accountServiceClient;
	}

	@Override
	public void accept(Message<TransactionNotificationDto> transactionNotificationDtoMessage) {
		System.out.print("Received Transaction notification with id ["+transactionNotificationDtoMessage.getPayload().getTransactionId()+"]");
		// TransactionNotificationDto
		TransactionNotificationDto transactionNotificationDto = transactionNotificationDtoMessage.getPayload();
		// Fieng accountServiceClient.getTraction - TransactionDTO
		TransactionDto transactionDto = accountServiceClient.getTransaction(transactionNotificationDto.getTransactionId());

		System.out.println("Recieved from Account Client"+transactionDto);
		// SMTP to send via email

	}
}
