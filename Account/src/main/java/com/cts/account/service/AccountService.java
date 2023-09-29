package com.cts.account.service;


import java.math.BigDecimal;

import com.cts.account.model.Account;
import com.cts.account.model.AccountType;
import com.cts.account.model.CreateAccountRequest;
import com.cts.account.model.CreateAccountResponse;

public interface AccountService {
	
	public CreateAccountResponse createAccount(CreateAccountRequest createAccountRequest);
	public Account findSavingsAccounts(Long customerId);	
	public Account viewCustomerByAccountNoAndAccountType(Long accountNo, AccountType accountType);
	void deleteAllAccountsByAccountNo(Long accountNo);
	public Long getgetCustomerIdByAccountNo(Long accountNo);
	public void deposit(Long accountNo, AccountType accounttype, BigDecimal amount);

}
