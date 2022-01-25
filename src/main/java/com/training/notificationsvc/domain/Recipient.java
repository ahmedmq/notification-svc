package com.training.notificationsvc.domain;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import org.springframework.data.annotation.Id;

@Data
// @Getter, @Setter, @RequiredArgsConstructor. @ToSring, @EqualsAndHashcode
public class Recipient {

	@Id
	private final String accountId;
	private final String name;
	private final LocalDateTime createAt;

}
