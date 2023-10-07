package com.cts.account.feignClient;

import java.math.BigDecimal;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@FeignClient(name = "transaction-service", url = "http://localhost:8070")
public interface TransactionFeignClient {
	
	@PostMapping("/api/transactions/deposit")
	<CreateTransactionRequest, TransactionStatus> void deposit(@RequestBody CreateTransactionRequest transaction);
	
	@PostMapping("/api/transactions/withdraw")
	<CreateTransactionRequest, TransactionStatus> void withdraw(@RequestBody CreateTransactionRequest transaction);
	
	
	
}
