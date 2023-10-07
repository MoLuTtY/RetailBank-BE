package com.cts.transaction.model;

import java.math.BigDecimal;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CreateTransactionRequest {
	
	private Date transactionDate;
	private Long accountId;
	private Long customerId;
	private Long sourceAccountNo;
	private AccountType sourceAccountType;
    private Long targetAccountNo;
    private BigDecimal withdrawalAmount;
    private BigDecimal transferAmount;
    private BigDecimal depositAmount;
    private BigDecimal closingBalance;
}
