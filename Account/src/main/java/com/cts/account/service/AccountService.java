package com.cts.account.service;


import java.math.BigDecimal;

import com.cts.account.model.Account;
import com.cts.account.model.AccountType;
import com.cts.account.model.CreateAccountRequest;
import com.cts.account.model.CreateAccountResponse;

public interface AccountService {
	
	public CreateAccountResponse createAccount(String token, CreateAccountRequest createAccountRequest);
	public Account findSavingsAccounts(String token,Long customerId);	
	public Account viewCustomerByAccountNoAndAccountType(String token, Long accountNo, AccountType accountType);
	void deleteAllAccountsByAccountNo(String token, Long accountNo);
	public Long getgetCustomerIdByAccountNo(String token, Long accountNo);
	public void deposit(String token, Long accountNo, AccountType accounttype, BigDecimal amount);
	public BigDecimal getCurrentBalance(String token, Long accountNo, AccountType accountType);
	public void updateCurrentBalance(String token,Long accountNo, AccountType accountType, BigDecimal newBalance);
	public Account findCurrentAccounts(String token, Long customerId);
	public void withdraw( String token, Long accountNo, AccountType accountType, BigDecimal amount);
	public Long getAccount(String token, Long accountNo, AccountType accountType);
	
}
