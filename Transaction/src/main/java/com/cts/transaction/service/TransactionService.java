package com.cts.transaction.service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import com.cts.transaction.model.AccountType;
import com.cts.transaction.model.AuthenticationResponse;
import com.cts.transaction.model.CreateTransactionRequest;
import com.cts.transaction.model.GetTransactionsResponse;
import com.cts.transaction.model.TransactionStatus;

public interface TransactionService {

	TransactionStatus deposit(String token, CreateTransactionRequest transaction);
	void deleteTransactions(String token, Long accountNo);
	TransactionStatus withdraw(String token,CreateTransactionRequest transaction);
	TransactionStatus trasfer(String token,Long source_account_no, AccountType source_account_type, Long target_account_no,
			BigDecimal amount);
	List<GetTransactionsResponse> getTransactions(String token, Long customerId);
	List<GetTransactionsResponse> getTransactionsByDateRange(String token, Long customerId, String dateFrom, String dateTo) throws ParseException, com.google.protobuf.TextFormat.ParseException;
	AuthenticationResponse hasPermission(String token);
	AuthenticationResponse hasCustomerPermission(String token);
    AuthenticationResponse hasEmployeePermission(String token);
}
