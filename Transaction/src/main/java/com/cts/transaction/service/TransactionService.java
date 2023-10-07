package com.cts.transaction.service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import com.cts.transaction.model.AccountType;
import com.cts.transaction.model.CreateTransactionRequest;
import com.cts.transaction.model.GetTransactionsResponse;
import com.cts.transaction.model.TransactionStatus;

public interface TransactionService {

	TransactionStatus deposit(CreateTransactionRequest transaction);
	void deleteTransactions(Long accountNo);
	TransactionStatus withdraw(CreateTransactionRequest transaction);
	TransactionStatus trasfer(Long source_account_no, AccountType source_account_type, Long target_account_no,
			BigDecimal amount);
	List<GetTransactionsResponse> getTransactions(Long customerId);
	List<GetTransactionsResponse> getTransactionsByDateRange(Long customerId, String dateFrom, String dateTo) throws ParseException, com.google.protobuf.TextFormat.ParseException;

}
