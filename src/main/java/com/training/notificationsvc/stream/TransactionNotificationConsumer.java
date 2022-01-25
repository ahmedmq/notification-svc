package com.training.notificationsvc.stream;

import java.util.function.Consumer;

import com.training.notificationsvc.dto.TransactionNotificationDto;

import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Component
public class TransactionNotificationConsumer implements Consumer<Message<TransactionNotificationDto>> {

	@Override
	public void accept(Message<TransactionNotificationDto> transactionNotificationDtoMessage) {
		System.out.print("Received Transaction notification with id ["+transactionNotificationDtoMessage.getPayload().getTransactionId()+"]");
	}
}
