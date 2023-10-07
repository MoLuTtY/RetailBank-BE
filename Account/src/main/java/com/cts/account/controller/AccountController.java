package com.cts.account.controller;


import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cts.account.model.Account;
import com.cts.account.model.AccountType;
import com.cts.account.model.CreateAccountRequest;
import com.cts.account.model.CreateAccountResponse;
import com.cts.account.service.AccountService;


@RestController
@RequestMapping("/api/accounts")
@CrossOrigin
public class AccountController {
	
	@Autowired
	private AccountService accountService;
	
	@PostMapping("/create-account")
	public ResponseEntity<CreateAccountResponse> createCustomer(@RequestBody CreateAccountRequest createAccountRequest) {
		CreateAccountResponse response = accountService.createAccount(createAccountRequest);
        if (response.getAccountNo() != null) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
	
	@GetMapping("/find-savings-accounts/{customerId}")
	public ResponseEntity<Account> findSavingsAccounts(@PathVariable Long customerId) {
		Account response = accountService.findSavingsAccounts(customerId);
		if (response!= null) {
            return ResponseEntity.ok(response);
        } else {
        	return ResponseEntity.notFound().build();
        }
	}
	
	@GetMapping("/find-current-accounts/{customerId}")
	public ResponseEntity<Account> findCurrentAccounts(@PathVariable Long customerId) {
		Account response = accountService.findCurrentAccounts(customerId);
		if (response!= null) {
            return ResponseEntity.ok(response);
        } else {
        	return ResponseEntity.notFound().build();
        }
	}
	
	@GetMapping("/viewCustomer-by-accountNo-and-accountType/{accountNo}/{accountType}")
	public ResponseEntity<Account> viewCustomerByAccountNoAndAccountType(@PathVariable Long accountNo, @PathVariable AccountType accountType) {
		Account response = accountService.viewCustomerByAccountNoAndAccountType(accountNo,accountType);
		System.out.println(response);
		if (response!= null) {
            return ResponseEntity.ok(response);
        } else {
        	return ResponseEntity.notFound().build();
        }
	}
	
	@DeleteMapping("/delete-account/{accountNo}")
    public void deleteAllAccountsByAccountNo(@PathVariable Long accountNo) {
        accountService.deleteAllAccountsByAccountNo(accountNo);
    }
	
	@GetMapping("/get-customerId/{accountNo}")
	public Long getCustomerIdByAccountNo(@PathVariable Long accountNo) {
		return accountService.getgetCustomerIdByAccountNo(accountNo);
	}
	
	@PostMapping("/deposit/{accountNo}/{accountType}/{amount}")
	public String deposit(@PathVariable Long accountNo,@PathVariable AccountType accountType, @PathVariable BigDecimal amount) {
		accountService.deposit(accountNo, accountType, amount);
		return "Status : Deposit Successful";
	}
	
	@GetMapping("/current-balance/{accountNo}/{accountType}")
	public BigDecimal getCurrentBalance(@PathVariable Long accountNo, @PathVariable AccountType accountType) {
		return accountService.getCurrentBalance(accountNo,accountType);
	}
	
	@PutMapping("/update-currentBalance/{accountNo}/{accountType}/{newBalance}")
	public void updateCurrentBalance(@PathVariable Long accountNo, @PathVariable AccountType accountType, @PathVariable BigDecimal newBalance) {
		 accountService.updateCurrentBalance(accountNo,accountType,newBalance);
	}
	
	@PostMapping("/withdraw/{accountNo}/{accountType}/{amount}")
	public String withdraw(@PathVariable Long accountNo,@PathVariable AccountType accountType, @PathVariable BigDecimal amount) {
		accountService.withdraw(accountNo, accountType, amount);
		return "Status : Withdrawal Successful";
	}
	
	@GetMapping("/getAccount/{accountNo}/{accountType}")
	public Long getAccount(@PathVariable Long accountNo, @PathVariable AccountType accountType) {
		return accountService.getAccount(accountNo,accountType);
	}
	
	
	
	
	
}
