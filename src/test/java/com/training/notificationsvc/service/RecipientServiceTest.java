package com.training.notificationsvc.service;

import java.time.LocalDateTime;
import java.util.Optional;

import com.training.notificationsvc.domain.Recipient;
import com.training.notificationsvc.dto.RecipientDto;
import com.training.notificationsvc.mapper.RecipientMapper;
import com.training.notificationsvc.mapper.RecipientMapperImpl;
import com.training.notificationsvc.repository.RecipientRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@Import(RecipientMapperImpl.class)
@ExtendWith(SpringExtension.class)
public class RecipientServiceTest {

	@Mock
	RecipientRepository recipientRepository;
	@Autowired
	RecipientMapper mapper;

	RecipientService cut;

	@BeforeEach
	void setUp() {
		cut = new RecipientService(recipientRepository,mapper);
	}

	@Test
	void shouldReturnRecipientDtoWhenAccountIdExists() {

		Recipient recipient = new Recipient("ACC1234567", "Mark", LocalDateTime.now());
		Mockito.when(recipientRepository.findById(ArgumentMatchers.anyString())).thenReturn(Optional.of(recipient));

		RecipientDto recipientDto = cut.getRecipient(recipient.getAccountId());

		Assertions.assertThat(recipientDto).isNotNull();
		Assertions.assertThat(recipientDto.getId()).isEqualTo(recipient.getAccountId());
		Assertions.assertThat(recipientDto.getName()).isEqualTo(recipient.getName());
		Assertions.assertThat(recipientDto.getCreateAt()).isNotNull();

	}

	@Test
	void shouldThrowExceptionWhenAccountIdNotExist() {
		// Exercise - TODO
	}
}
