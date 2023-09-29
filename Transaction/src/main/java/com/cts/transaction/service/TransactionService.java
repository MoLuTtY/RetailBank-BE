package com.cts.transaction.service;

import com.cts.transaction.model.CreateTransactionRequest;
import com.cts.transaction.model.TransactionStatus;

public interface TransactionService {

	TransactionStatus deposit(CreateTransactionRequest transaction);
	void deleteTransactions(Long accountNo);

}