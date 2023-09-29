package com.cts.transaction.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cts.transaction.model.CreateTransactionRequest;
import com.cts.transaction.model.TransactionStatus;
import com.cts.transaction.service.TransactionService;

@RestController
@RequestMapping("/api/transactions")
@CrossOrigin
public class TransactionController {
	
	@Autowired
	private TransactionService transactionService;
	
	@PostMapping("/deposit")
	public ResponseEntity<TransactionStatus> deposit(@RequestBody CreateTransactionRequest transaction) {
		TransactionStatus status = transactionService.deposit(transaction);
		
		if (status.getTransactionId() != null) {
            return ResponseEntity.ok(status);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(status);
        }
	}
	
	@DeleteMapping("/delete-transactions/{accountNo}")
	public void deleteTransactions(@PathVariable Long accountNo) {
		transactionService.deleteTransactions(accountNo);
		
	}

}