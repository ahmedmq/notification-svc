package com.training.notificationsvc.client;

import java.math.BigDecimal;

import com.sun.jna.platform.win32.Advapi32Util.Account;
import com.training.notificationsvc.dto.TransactionDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "account-service", url = "${account-service-url}", fallback = AccountServiceClient.AccountServiceClientFallback.class )
@Primary
public interface AccountServiceClient {

    @GetMapping("/api/transactions/{transactionId}")
    TransactionDto getTransaction(@PathVariable Long transactionId);

	@Component
	static class AccountServiceClientFallback implements AccountServiceClient{

		@Override
		public TransactionDto getTransaction(Long transactionId) {

			System.out.println("Fallback triggered: Returning dummy transactionDto");

			TransactionDto dummy = new TransactionDto();
			dummy.setAccountId(null);
			dummy.setTransactionId(transactionId);
			dummy.setAmount(BigDecimal.ZERO);
			dummy.setBalance(BigDecimal.ZERO);
			dummy.setDescription("");
			dummy.setCreateAt("");

			return dummy;
		}
	}

}
