package com.cts.transaction.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cts.transaction.model.CreateTransactionRequest;
import com.cts.transaction.model.Transaction;
import com.cts.transaction.model.TransactionStatus;
import com.cts.transaction.repository.TransactionRepository;

import jakarta.transaction.Transactional;

@Service
public class TransactionServiceImpl implements TransactionService{
	
	@Autowired
	private TransactionRepository transactionRepository;
	

	@Override
	public TransactionStatus deposit(CreateTransactionRequest transaction) {
		Transaction transactionObj = new Transaction();
		transactionObj.setAccountId(transaction.getAccountId());
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
	public void deleteTransactions(Long accountNo) {
		
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

}