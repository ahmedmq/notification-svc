package com.training.notificationsvc.dto;

import com.training.notificationsvc.domain.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDto {
    private Long transactionId;
    private TransactionType transactionType;
    private String accountId;
    private BigDecimal amount;
    private BigDecimal balance;
    private String description;
    private String createAt;
}
