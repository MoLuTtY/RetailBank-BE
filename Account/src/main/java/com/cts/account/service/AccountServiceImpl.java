package com.cts.account.service;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cts.account.exception.AccountNotFoundException;
import com.cts.account.exception.MinimumBalanceException;

import com.cts.account.feignClient.RuleFeignClient;
import com.cts.account.feignClient.TransactionFeignClient;
import com.cts.account.model.Account;
import com.cts.account.model.AccountId;
import com.cts.account.model.AccountType;
import com.cts.account.model.CreateAccountRequest;
import com.cts.account.model.CreateAccountResponse;
import com.cts.account.model.CreateTransactionRequest;
import com.cts.account.model.RuleStatus;
import com.cts.account.repository.AccountRepository;


import jakarta.transaction.Transactional;

@Service
public class AccountServiceImpl implements AccountService{
	
	@Autowired
	private AccountRepository accountRepository;
	
	private final TransactionFeignClient transactionFeignClient;
	private final RuleFeignClient ruleFeignClient;
	

    @Autowired
    public AccountServiceImpl(TransactionFeignClient transactionFeignClient, RuleFeignClient ruleFeignClient) {
        this.transactionFeignClient = transactionFeignClient;
        this.ruleFeignClient = ruleFeignClient;
       
    }
	
	private Long generateNextAccountNumber() {
	    Account latestAccount = accountRepository.findTopByOrderByAccountIdDesc();
	    if (latestAccount != null && latestAccount.getAccountId() != null) {
	        Long previousAccountNumber = latestAccount.getAccountId().getAccountNo();
	        Long nextAccountNumber = previousAccountNumber + 1;
	        return nextAccountNumber;
	    } else {
	        return 1_000_000L; 
	    }
	}

	@Override
	public CreateAccountResponse createAccount(String token, CreateAccountRequest createAccountRequest) {
		
		AccountId savingsAccountId = new AccountId();
		savingsAccountId.setAccountNo(generateNextAccountNumber());
	    savingsAccountId.setAccountType(AccountType.SAVINGS);

	    Account savingsAccount = new Account();
	    savingsAccount.setAccountId(savingsAccountId);
		savingsAccount.setCustomerId(createAccountRequest.getCustomerId());
		savingsAccount.setOpeningAmount(createAccountRequest.getOpeningAmount());
		savingsAccount.setCurrentBalance(createAccountRequest.getOpeningAmount());
		savingsAccount.setInitiationDate(createAccountRequest.getInitiationDate());

        
        AccountId currentAccountId = new AccountId();
        currentAccountId.setAccountNo(generateNextAccountNumber());
        currentAccountId.setAccountType(AccountType.CURRENT);
        
        Account currentAccount = new Account();
        currentAccount.setAccountId(currentAccountId);
        currentAccount.setCustomerId(createAccountRequest.getCustomerId());
        currentAccount.setOpeningAmount(createAccountRequest.getOpeningAmount());
        currentAccount.setCurrentBalance(createAccountRequest.getOpeningAmount());
        currentAccount.setInitiationDate(createAccountRequest.getInitiationDate());
        
        accountRepository.save(savingsAccount);
        accountRepository.save(currentAccount);

        CreateAccountResponse response = new CreateAccountResponse(savingsAccountId.getAccountNo(), "Savings and Current account created successfully");

        return response;
	}

	@Override
	public Account findSavingsAccounts(String token, Long customerId) {
		return accountRepository.findSavingsAccountByCustomerId(customerId);
		
	}

	@Override
	public Account viewCustomerByAccountNoAndAccountType(String token, Long accountNo, AccountType accountType) {
	    Account account = accountRepository.viewCustomerByAccountNoAndAccountType(accountNo, accountType);
	    
	    if (account != null) {
	        return account;
	    } else {
	        throw new AccountNotFoundException("Account not found for accountNo: " + accountNo);
	    }
	}
	
	@Transactional
    public void deleteAllAccountsByAccountNo(String token, Long accountNo) {
        accountRepository.deleteAllByAccountIdAccountNo(accountNo);
    }

	@Override
	public Long getgetCustomerIdByAccountNo(String token,Long accountNo) {
		Account account = accountRepository.getCustomerId(accountNo);
		return account.getCustomerId();
	}

	@Override
	public void deposit(String token, Long accountNo, AccountType accountType, BigDecimal amount) {
		Account account = accountRepository.getAccountByNoAndType(accountNo,accountType);

		if(account != null) {
			BigDecimal currentBalance = account.getCurrentBalance();
			currentBalance = currentBalance.add(amount);
			account.setCurrentBalance(currentBalance);
			accountRepository.save(account);
			
			Date currentDate = new Date();
						
			CreateTransactionRequest request = new CreateTransactionRequest();
			request.setAccountId(account.getId());
			request.setCustomerId(account.getCustomerId());
			request.setTransactionDate(currentDate);
			request.setSourceAccountNo(account.getAccountId().getAccountNo());
			request.setSourceAccountType(account.getAccountId().getAccountType());
			request.setDepositAmount(amount);	
			request.setClosingBalance(currentBalance);
			transactionFeignClient.deposit(token, request);			
		} else {
			throw new AccountNotFoundException("Account not found");
			
			
		}				
	}

	@Override
	public BigDecimal getCurrentBalance(String token, Long accountNo, AccountType accountType) {
		Account account =  accountRepository.getAccountByNoAndType(accountNo,accountType);
		return account.getCurrentBalance();
	}

	@Override
	public void updateCurrentBalance(String token, Long accountNo, AccountType accountType, BigDecimal newBalance) {
		Account account =  accountRepository.getAccountByNoAndType(accountNo,accountType);
		if(account != null) {
			account.setCurrentBalance(newBalance);
		}
		accountRepository.save(account);
	}

	@Override
	public Account findCurrentAccounts(String token, Long customerId) {
		return accountRepository.findCurrentAccountByCustomerId(customerId);
	}

	@Override
	public void withdraw( String token, Long accountNo, AccountType accountType, BigDecimal amount) {
		Account account = accountRepository.getAccountByNoAndType(accountNo, accountType);
		if(account!=null) {
			BigDecimal balance = account.getCurrentBalance().subtract(amount);
			RuleStatus status = ruleFeignClient.evaluateMinBal(token,balance);
			
			if (status == RuleStatus.ALLOWED) {
               account.setCurrentBalance(balance);
               accountRepository.save(account);
               
               CreateTransactionRequest request = new CreateTransactionRequest();
               Date currentDate = new Date();
               
               request.setAccountId(account.getId());
               request.setCustomerId(account.getCustomerId());
               request.setWithdrawalAmount(amount);
               request.setClosingBalance(balance);
               request.setSourceAccountNo(accountNo);
               request.setSourceAccountType(accountType);
               request.setTransactionDate(currentDate);
               transactionFeignClient.withdraw(token,request);	
            } else {
            
                throw new MinimumBalanceException("Withdrawal not allowed due to minimum balance rule");
            }
		} else {
			throw new AccountNotFoundException("Account not found");
		}
		
	}

	@Override
	public Long getAccount(String token,Long accountNo, AccountType accountType) {
		Account account = accountRepository.getAccountByNoAndType(accountNo, accountType);
		return account.getId();
	}

	


	
}
