package com.cts.transaction.service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cts.transaction.exception.AccessDeniedException;
import com.cts.transaction.feignClient.AccountFeignClient;
import com.cts.transaction.feignClient.AuthFeignClient;
import com.cts.transaction.feignClient.RuleFeignClient;
import com.cts.transaction.model.AccountType;
import com.cts.transaction.model.AuthenticationResponse;
import com.cts.transaction.model.CreateTransactionRequest;
import com.cts.transaction.model.GetTransactionsResponse;
import com.cts.transaction.model.RuleStatus;
import com.cts.transaction.model.Transaction;
import com.cts.transaction.model.TransactionStatus;
import com.cts.transaction.repository.TransactionRepository;
import com.google.protobuf.TextFormat.ParseException;

import jakarta.transaction.Transactional;


@Service
public class TransactionServiceImpl implements TransactionService{
	
	@Autowired
	private TransactionRepository transactionRepository;
	
	private final AccountFeignClient accountFeignClient;
	private final RuleFeignClient ruleFeignClient;
	private final AuthFeignClient authFeignClient;
	
    @Autowired
    public TransactionServiceImpl(AccountFeignClient accountFeignClient,RuleFeignClient ruleFeignClient,AuthFeignClient authFeignClient) {
        this.accountFeignClient = accountFeignClient;
        this.ruleFeignClient = ruleFeignClient;
        this.authFeignClient = authFeignClient;
    }
    
    @Override
   	public AuthenticationResponse hasPermission(String token) {
   		return authFeignClient.getValidity(token);
   	}
       
       @Override
   	public AuthenticationResponse hasCustomerPermission(String token) {
   		AuthenticationResponse validity = authFeignClient.getValidity(token);
   		if (!authFeignClient.getRole(token, validity.getUserid()).equals("CUSTOMER"))
   			throw new AccessDeniedException("NOT ALLOWED");
   		else
   			return validity;
   	}
       
       @Override
   	public AuthenticationResponse hasEmployeePermission(String token) {
   		AuthenticationResponse validity = authFeignClient.getValidity(token);

   		if (!authFeignClient.getRole(token, validity.getUserid()).equals("EMPLOYEE"))
   			throw new AccessDeniedException("NOT ALLOWED");
   		else
   			return validity;
   	}
	

	@Override
	public TransactionStatus deposit(String token, CreateTransactionRequest transaction) {
		Transaction transactionObj = new Transaction();
		transactionObj.setAccountId(transaction.getAccountId());
		transactionObj.setCustomerId(transaction.getCustomerId());
		transactionObj.setTransactionDate(transaction.getTransactionDate());
		transactionObj.setSourceAccountNo(transaction.getSourceAccountNo());
		transactionObj.setSourceAccountType(transaction.getSourceAccountType());
		transactionObj.setTargetAccountNo(transaction.getTargetAccountNo());
		transactionObj.setTransferAmount(transaction.getTransferAmount());
		transactionObj.setWithdrawalAmount(transaction.getWithdrawalAmount());
		transactionObj.setDepositAmount(transaction.getDepositAmount());
		transactionObj.setClosingBalance(transaction.getClosingBalance());
		transactionRepository.save(transactionObj);
		
		TransactionStatus status = new TransactionStatus();
		status.setTransactionId(transactionObj.getTransactionId());
		status.setStatus("Deposit successful");
		
		return status;
		
	}

	@SuppressWarnings("deprecation")
	@Transactional
	@Override
	public void deleteTransactions(String token, Long accountNo) {
		
        List<Transaction> transactions = transactionRepository.findAll();
        List<Transaction> transactionsToDelete = new ArrayList<>();
        for(Transaction transaction : transactions ) {
        	Long transactionAccountNo = transaction.getSourceAccountNo();
   
        	 if (transactionAccountNo.equals(accountNo)) {
                 transactionsToDelete.add(transaction);
             }
        }
        transactionRepository.deleteInBatch(transactionsToDelete);
	}

	@Override
	public TransactionStatus withdraw(String token,CreateTransactionRequest transaction) {
		Transaction transactionObj = new Transaction();
		transactionObj.setAccountId(transaction.getAccountId());
		transactionObj.setCustomerId(transaction.getCustomerId());
		transactionObj.setTransactionDate(transaction.getTransactionDate());
		transactionObj.setSourceAccountNo(transaction.getSourceAccountNo());
		transactionObj.setSourceAccountType(transaction.getSourceAccountType());
		transactionObj.setTargetAccountNo(transaction.getTargetAccountNo());
		transactionObj.setTransferAmount(transaction.getTransferAmount());
		transactionObj.setWithdrawalAmount(transaction.getWithdrawalAmount());
		transactionObj.setDepositAmount(transaction.getDepositAmount());
		transactionObj.setClosingBalance(transaction.getClosingBalance());
		transactionRepository.save(transactionObj);
		
		TransactionStatus status = new TransactionStatus();
		status.setTransactionId(transactionObj.getTransactionId());
		status.setStatus("Withdrawal successful");
		
		return status;
	}

	@Override
	public TransactionStatus trasfer(String token,Long source_account_no, AccountType source_account_type, Long target_account_no,
			BigDecimal amount) {

		BigDecimal sourceCurrentBalance = accountFeignClient.getCurrentBalance(token,source_account_no,source_account_type);
		BigDecimal sourceNewBalance = sourceCurrentBalance.subtract(amount);
		
		BigDecimal targetCurrentBalance = accountFeignClient.getCurrentBalance(token,target_account_no, AccountType.SAVINGS);
		BigDecimal targetNewBalance = targetCurrentBalance.add(amount);
		
		RuleStatus status = ruleFeignClient.evaluateMinBal(token,sourceNewBalance);
		TransactionStatus status1 = new TransactionStatus();
		if (status == RuleStatus.ALLOWED) {
			accountFeignClient.updateCurrentBalance(token,source_account_no,source_account_type,sourceNewBalance);
			accountFeignClient.updateCurrentBalance(token,target_account_no, AccountType.SAVINGS, targetNewBalance);
			
			Date currentDate = new Date();
			
			Transaction transaction = new Transaction();
			transaction.setAccountId(accountFeignClient.getAccount(token,source_account_no, source_account_type));
			transaction.setCustomerId(accountFeignClient.getCustomerIdByAccountNo(token,source_account_no));
			transaction.setSourceAccountNo(source_account_no);
			transaction.setSourceAccountType(source_account_type);
			transaction.setTargetAccountNo(target_account_no);
			transaction.setTransferAmount(amount);
			transaction.setTransactionDate(currentDate);
			transaction.setClosingBalance(sourceNewBalance);
			transactionRepository.save(transaction);  
					
			status1.setTransactionId(transaction.getTransactionId());
			status1.setStatus("Withdrawal successful");
			
		} else {
			
		}
		return status1;		
	}


	@Override
	public List<GetTransactionsResponse> getTransactions(String token, Long customerId) {
		List<GetTransactionsResponse> result = new ArrayList<>();
		List<Transaction> transactions = transactionRepository.getTransactionsByCustomerId(customerId);
		
		for(Transaction transaction : transactions) {
			GetTransactionsResponse response = new GetTransactionsResponse();
			
			Date date = transaction.getTransactionDate();
			LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			response.setTransactionDate(localDate);
			
			response.setSourceAccountType(transaction.getSourceAccountType());
			response.setTargetAccountNo(transaction.getTargetAccountNo());
			response.setDepositAmount(transaction.getDepositAmount());
			response.setWithdrawalAmount(transaction.getWithdrawalAmount());
			response.setTransferAmount(transaction.getTransferAmount());
			response.setClosingBalance(transaction.getClosingBalance());
			result.add(response);
		}
		return result;
	}

	@Override
	public List<GetTransactionsResponse> getTransactionsByDateRange(String token, Long customerId, String dateFrom, String dateTo) throws java.text.ParseException, ParseException {
	    List<GetTransactionsResponse> transactions = getTransactions(token, customerId);
	    List<GetTransactionsResponse> filteredTransactions = new ArrayList<>();

	    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	    Date fromDate = dateFormat.parse(dateFrom);
		Date toDate = dateFormat.parse(dateTo);

		for (GetTransactionsResponse transaction : transactions) {
		    LocalDate transactionDate = transaction.getTransactionDate();
		    LocalDate dateFromLocal = fromDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		    LocalDate dateToLocal = toDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

		    if ((transactionDate.isEqual(dateFromLocal) || transactionDate.isAfter(dateFromLocal)) &&
		        transactionDate.isBefore(dateToLocal)) {
		        filteredTransactions.add(transaction);
		    }
		}

	    return filteredTransactions;
	}


	
}
