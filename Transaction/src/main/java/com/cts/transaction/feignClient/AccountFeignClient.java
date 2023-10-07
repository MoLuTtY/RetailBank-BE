package com.cts.transaction.feignClient;

import java.math.BigDecimal;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import com.cts.transaction.model.AccountType;


@FeignClient(name = "account-service", url = "http://localhost:8090") 
public interface AccountFeignClient {
	
	@GetMapping("/api/accounts/current-balance/{accountNo}/{accountType}")
	BigDecimal getCurrentBalance(@PathVariable Long accountNo, @PathVariable AccountType accountType);

	@GetMapping("/api/accounts/getAccount/{accountNo}/{accountType}")
	public Long getAccount(@PathVariable Long accountNo, @PathVariable AccountType accountType);
	
	@GetMapping("/api/accounts/get-customerId/{accountNo}")
	Long getCustomerIdByAccountNo(@PathVariable Long accountNo);

	@PutMapping("/api/accounts/update-currentBalance/{accountNo}/{accountType}/{newBalance}")
	 void updateCurrentBalance(@PathVariable Long accountNo, @PathVariable AccountType accountType, @PathVariable BigDecimal newBalance);
}
