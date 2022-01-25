package com.training.notificationsvc.client;

import com.training.notificationsvc.dto.TransactionDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "account-service", url = "${account-service-url}")
public interface AccountServiceClient {

    @GetMapping("/api/transactions/{transactionId}")
    TransactionDto getTransaction(@PathVariable long transactionId);
}
