package com.cts.account.feignClient;

import org.springframework.cloud.openfeign.FeignClient;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;


@FeignClient(name = "transaction-service", url = "http://localhost:8070")
public interface TransactionFeignClient {
	
	@PostMapping("/api/transactions/deposit")
	<CreateTransactionRequest, TransactionStatus> void deposit(@RequestHeader("Authorization") String token, @RequestBody CreateTransactionRequest transaction);
	
	@PostMapping("/api/transactions/withdraw")
	<CreateTransactionRequest, TransactionStatus> void withdraw(@RequestHeader("Authorization") String token,@RequestBody CreateTransactionRequest transaction);	
}
