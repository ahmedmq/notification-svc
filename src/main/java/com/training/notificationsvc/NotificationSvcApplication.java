package com.training.notificationsvc;

import java.util.function.Consumer;

import com.training.notificationsvc.dto.TransactionNotificationDto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;

@SpringBootApplication
public class NotificationSvcApplication {

	public static void main(String[] args) {
		SpringApplication.run(NotificationSvcApplication.class, args);
	}

}
