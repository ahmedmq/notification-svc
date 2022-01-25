package com.training.notificationsvc.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import com.training.notificationsvc.domain.Recipient;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest(properties = "spring.mongodb.embedded.version=4.0.12")
public class RecipientRepositoryTest {

	@Autowired
	RecipientRepository recipientRepository;

	@Test
	void shouldReturnRecipientWhenAccountIdExists() {

		// Save a Recipient -- Arrange
		Recipient recipient = new Recipient("ACC1234567", "Mark", LocalDateTime.now());
		recipientRepository.save(recipient);

		// Fetch the recipient -- Act
		Optional<Recipient> foundRecipient = recipientRepository.findById("ACC1234567");

		// Assertions -- Assert
		assertThat(foundRecipient).isPresent();
		assertThat(foundRecipient.get()).satisfies(t-> {
			assertThat(t.getAccountId()).isEqualTo("ACC1234567");
			assertThat(t.getName()).isEqualTo("Mark");
			assertThat(t.getCreateAt()).isNotNull();
		});


	}
}
