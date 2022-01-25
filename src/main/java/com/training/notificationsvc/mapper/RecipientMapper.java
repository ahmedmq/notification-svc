package com.training.notificationsvc.mapper;

import java.time.LocalDateTime;

import com.fasterxml.jackson.datatype.jsr310.ser.DurationSerializer;
import com.training.notificationsvc.domain.Recipient;
import com.training.notificationsvc.dto.RecipientDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface RecipientMapper {

	@Mapping(source = "id", target="accountId")
	@Mapping(target = "createAt", defaultExpression = "java(java.time.LocalDateTime.now())")
	Recipient toRecipient(RecipientDto recipientDto);

	@Mapping(source = "accountId", target="id")
	@Mapping(target="createAt", dateFormat = "yyyy-MM-dd HH:mm:ss")
	RecipientDto toRecipientDto(Recipient recipient);


}
