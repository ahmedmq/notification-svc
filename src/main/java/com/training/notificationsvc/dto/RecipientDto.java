package com.training.notificationsvc.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class RecipientDto {
	private String accountId;
	private String name;
	private LocalDateTime createAt;
}
