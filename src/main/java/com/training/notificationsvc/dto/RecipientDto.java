package com.training.notificationsvc.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class RecipientDto {
	private final String id;
	private final String name;
	private final String createAt;

}
