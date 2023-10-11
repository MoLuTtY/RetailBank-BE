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
import org.springframework.web.bind.annotation.RequestHeader;
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
	public ResponseEntity<CreateAccountResponse> createAccount(@RequestHeader("Authorization") String token,@RequestBody CreateAccountRequest createAccountRequest) {
		CreateAccountResponse response = accountService.createAccount(token,createAccountRequest);
        if (response.getAccountNo() != null) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
	
	@GetMapping("/find-savings-accounts/{customerId}")
	public ResponseEntity<Account> findSavingsAccounts(@RequestHeader("Authorization") String token,@PathVariable Long customerId) {
		Account response = accountService.findSavingsAccounts(token, customerId);
		if (response!= null) {
            return ResponseEntity.ok(response);
        } else {
        	return ResponseEntity.notFound().build();
        }
	}
	
	@GetMapping("/find-current-accounts/{customerId}")
	public ResponseEntity<Account> findCurrentAccounts(@RequestHeader("Authorization") String token, @PathVariable Long customerId) {
		Account response = accountService.findCurrentAccounts(token, customerId);
		if (response!= null) {
            return ResponseEntity.ok(response);
        } else {
        	return ResponseEntity.notFound().build();
        }
	}
	
	@GetMapping("/viewCustomer-by-accountNo-and-accountType/{accountNo}/{accountType}")
	public ResponseEntity<Account> viewCustomerByAccountNoAndAccountType(@RequestHeader("Authorization") String token, @PathVariable Long accountNo, @PathVariable AccountType accountType) {
		Account response = accountService.viewCustomerByAccountNoAndAccountType(token, accountNo,accountType);
		System.out.println(response);
		if (response!= null) {
            return ResponseEntity.ok(response);
        } else {
        	return ResponseEntity.notFound().build();
        }
	}
	
	@DeleteMapping("/delete-account/{accountNo}")
    public void deleteAllAccountsByAccountNo(@RequestHeader("Authorization") String token, @PathVariable Long accountNo) {
        accountService.deleteAllAccountsByAccountNo(token, accountNo);
    }
	
	@GetMapping("/get-customerId/{accountNo}")
	public Long getCustomerIdByAccountNo(@RequestHeader("Authorization") String token,@PathVariable Long accountNo) {
		return accountService.getgetCustomerIdByAccountNo(token,accountNo);
	}
	
	@PostMapping("/deposit/{accountNo}/{accountType}/{amount}")
	public String deposit(@RequestHeader("Authorization") String token, @PathVariable Long accountNo,@PathVariable AccountType accountType, @PathVariable BigDecimal amount) {
		accountService.deposit(token, accountNo, accountType, amount);
		return "Status : Deposit Successful";
	}
	
	@GetMapping("/current-balance/{accountNo}/{accountType}")
	public BigDecimal getCurrentBalance(@RequestHeader("Authorization") String token, @PathVariable Long accountNo, @PathVariable AccountType accountType) {
		return accountService.getCurrentBalance(token,accountNo,accountType);
	}
	
	@PutMapping("/update-currentBalance/{accountNo}/{accountType}/{newBalance}")
	public void updateCurrentBalance(@RequestHeader("Authorization") String token,@PathVariable Long accountNo, @PathVariable AccountType accountType, @PathVariable BigDecimal newBalance) {
		 accountService.updateCurrentBalance(token,accountNo,accountType,newBalance);
	}
	
	@PostMapping("/withdraw/{accountNo}/{accountType}/{amount}")
	public String withdraw(@RequestHeader("Authorization") String token, @PathVariable Long accountNo,@PathVariable AccountType accountType, @PathVariable BigDecimal amount) {
		
		accountService.withdraw(token,accountNo, accountType, amount);
		return "Status : Withdrawal Successful";
	}
	
	@GetMapping("/getAccount/{accountNo}/{accountType}")
	public Long getAccount(@RequestHeader("Authorization") String token,@PathVariable Long accountNo, @PathVariable AccountType accountType) {
		return accountService.getAccount(token,accountNo,accountType);
	}
	
	
	
	
	
}
