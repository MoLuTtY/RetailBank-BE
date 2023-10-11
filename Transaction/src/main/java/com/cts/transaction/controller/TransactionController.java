package com.cts.transaction.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cts.transaction.model.AccountType;
import com.cts.transaction.model.CreateTransactionRequest;
import com.cts.transaction.model.GetTransactionsResponse;
import com.cts.transaction.model.TransactionStatus;
import com.cts.transaction.service.TransactionService;
import com.google.protobuf.TextFormat.ParseException;

@RestController
@RequestMapping("/api/transactions")
@CrossOrigin(origins = "http://localhost:3000")
public class TransactionController {
	
	@Autowired
	private TransactionService transactionService;
	
	@PostMapping("/deposit")
	public ResponseEntity<TransactionStatus> deposit(@RequestHeader("Authorization") String token, @RequestBody CreateTransactionRequest transaction) {
		TransactionStatus status = transactionService.deposit(token, transaction);
		
		if (status.getTransactionId() != null) {
            return ResponseEntity.ok(status);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(status);
        }
	}
	
	@PostMapping("/withdraw")
	public ResponseEntity<TransactionStatus> withdraw(@RequestHeader("Authorization") String token, @RequestBody CreateTransactionRequest transaction) {
		TransactionStatus status = transactionService.withdraw(token,transaction);
		
		if (status.getTransactionId() != null) {
            return ResponseEntity.ok(status);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(status);
        }
	}
	
	@DeleteMapping("/delete-transactions/{accountNo}")
	public void deleteTransactions(@RequestHeader("Authorization") String token, @PathVariable Long accountNo) {
		transactionService.deleteTransactions(token, accountNo);	
	}
	
	@PostMapping("/transfer/{source_account_no}/{source_account_type}/{target_account_no}/{amount}")
	public ResponseEntity<TransactionStatus> transfer(@RequestHeader("Authorization") String token, @PathVariable Long source_account_no, @PathVariable AccountType source_account_type, @PathVariable Long target_account_no, @PathVariable BigDecimal amount) {
		TransactionStatus status = transactionService.trasfer(token,source_account_no,source_account_type,target_account_no,amount);
		if (status.getTransactionId() != null) {
            return ResponseEntity.ok(status);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(status);
        }
		
	}
	
	
	@GetMapping("/get-transactions/{customerId}")
	public ResponseEntity<List<GetTransactionsResponse>> getTransactions(@RequestHeader("Authorization") String token, @PathVariable Long customerId) {
	    List<GetTransactionsResponse> response = transactionService.getTransactions(token, customerId);
	    
	    if (response != null && !response.isEmpty()) {
	        return ResponseEntity.ok(response);
	    } else {
	        return ResponseEntity.notFound().build(); 
	    }
	}
	
	@GetMapping("/get-transactions/{customerId}/{dateFrom}/{dateTo}")
	public ResponseEntity<List<GetTransactionsResponse>> getTransactionsByDateRange(@RequestHeader("Authorization") String token, @PathVariable Long customerId, @PathVariable String dateFrom, @PathVariable String dateTo) throws ParseException, java.text.ParseException {
	    List<GetTransactionsResponse> response = transactionService.getTransactionsByDateRange(token,customerId,dateFrom,dateTo);
	    
	    if (response != null && !response.isEmpty()) {
	        return ResponseEntity.ok(response);
	    } else {
	        return ResponseEntity.notFound().build(); 
	    }
	}


}
