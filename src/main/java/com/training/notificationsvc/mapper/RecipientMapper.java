package com.training.notificationsvc.mapper;

import com.training.notificationsvc.domain.Recipient;
import com.training.notificationsvc.dto.RecipientDto;
import org.mapstruct.Mapper;

@Mapper
public interface RecipientMapper {

	Recipient toRecipient(RecipientDto recipientDto);
}
