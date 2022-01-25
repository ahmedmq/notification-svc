package com.training.notificationsvc.mapper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.training.notificationsvc.domain.Recipient;
import com.training.notificationsvc.dto.RecipientDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@Import(RecipientMapperImpl.class)
class RecipientMapperTest {

	@Autowired
	RecipientMapper mapper;

	@Test
	void shouldReturnRecipientFromRecipientDto() {

		RecipientDto recipientDto = new RecipientDto("ACC1234567", "Mark",
				LocalDateTime.of(2022,1,1,1,1,1).toString());
		Recipient recipient = mapper.toRecipient(recipientDto);

		assertThat(recipient).isNotNull();
		assertThat(recipient.getAccountId()).isEqualTo("ACC1234567");
		assertThat(recipient.getName()).isEqualTo("Mark");
		assertThat(recipient.getCreateAt()).isEqualTo(LocalDateTime.of(2022,1,1,1,1,1));

	}

	@Test
	void shouldReturnRecipientDtoFromRecipient() {

		Recipient recipient = new Recipient("ACC1234567", "Mark",
				LocalDateTime.of(2022,1,1,1,1,1));

		RecipientDto recipientDto = mapper.toRecipientDto(recipient);

		assertThat(recipientDto).isNotNull();
		assertThat(recipientDto.getId()).isEqualTo("ACC1234567");
		assertThat(recipientDto.getName()).isEqualTo("Mark");
		assertThat(recipientDto.getCreateAt())
				.isEqualTo(LocalDateTime.of(2022,1,1,1,1,1)
						.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));


	}

	@Test
	void shouldSetDefaultCreatedAtWhenNoValueInDto() {
		RecipientDto recipientDto = new RecipientDto("ACC1234567", "David", null);

		Recipient recipient = mapper.toRecipient(recipientDto);

		assertThat(recipient).isNotNull();
		assertThat(recipient.getAccountId()).isEqualTo("ACC1234567");
		assertThat(recipient.getName()).isEqualTo("David");
		assertThat(recipient.getCreateAt()).isNotNull();


	}
}