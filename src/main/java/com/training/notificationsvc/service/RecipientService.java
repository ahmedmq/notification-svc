package com.training.notificationsvc.service;

import java.util.Optional;

import com.training.notificationsvc.domain.Recipient;
import com.training.notificationsvc.dto.RecipientDto;
import com.training.notificationsvc.exception.RecipientNotFoundException;
import com.training.notificationsvc.mapper.RecipientMapper;
import com.training.notificationsvc.repository.RecipientRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RecipientService {
	private final RecipientRepository recipientRepository;
	private final RecipientMapper recipientMapper;

	public RecipientDto getRecipient(String accountId) {
		Optional<Recipient> recipient = recipientRepository.findById(accountId);
		Recipient recipientDoc = recipient.orElseThrow(()->new RecipientNotFoundException());
		return recipientMapper.toRecipientDto(recipientDoc);
	}
}
