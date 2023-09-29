package com.cts.customer.feignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "transaction-service", url = "http://localhost:8070")
public interface TransactionFeignClient {
	
	@DeleteMapping("/api/transactions/delete-transactions/{accountNo}")
	public void deleteTransactions(@PathVariable Long accountNo);

}
