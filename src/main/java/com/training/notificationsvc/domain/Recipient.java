package com.training.notificationsvc.domain;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Data
// @Getter, @Setter, @RequiredArgsConstructor. @ToSring, @EqualsAndHashcode
public class Recipient {

	private String accountId;
	private String name;
	private LocalDateTime createAt;



}
