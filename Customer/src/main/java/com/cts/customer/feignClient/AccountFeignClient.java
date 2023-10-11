package com.cts.customer.feignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import com.cts.account.model.Account;
import com.cts.account.model.AccountType;


@FeignClient(name = "account-service", url = "http://localhost:8090") 
public interface AccountFeignClient {

	@GetMapping("/api/accounts/find-savings-accounts/{customerId}")
	Account getCustomerSavingsAccount(@RequestHeader("Authorization") String token, @PathVariable Long customerId);
	
	@GetMapping("/api/accounts/find-current-accounts/{customerId}")
	Account findCurrentAccounts(@RequestHeader("Authorization") String token, @PathVariable Long customerId);
	
	@GetMapping("/api/accounts/viewCustomer-by-accountNo-and-accountType/{accountNo}/{accountType}")
	Account viewCustomerByAccountNoAndAccountType(@RequestHeader("Authorization") String token, @PathVariable Long accountNo, @PathVariable AccountType accountType);
	
	@DeleteMapping("/api/accounts/delete-account/{accountNo}")
    void deleteAllAccountsByAccountNo(@RequestHeader("Authorization") String token, @PathVariable Long accountNo);
}
