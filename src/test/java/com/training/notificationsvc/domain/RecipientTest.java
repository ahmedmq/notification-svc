package com.training.notificationsvc.domain;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RecipientTest {

	@Test
	void testSample() {
		Recipient recipient = new Recipient("ACC12345", "Mark", LocalDateTime.now());

		System.out.println(recipient);

	}
}