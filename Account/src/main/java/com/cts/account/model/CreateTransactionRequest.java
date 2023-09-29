package com.cts.account.model;

import java.math.BigDecimal;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateTransactionRequest {
	
	private Date transactionDate;
	private Long accountId;
	private Long sourceAccountNo;
	private AccountType sourceAccountType;
    private Long targetAccountNo;
    private BigDecimal withdrawalAmount;
    private BigDecimal transferAmount;
    private BigDecimal depositAmount;
    private BigDecimal closingBalance;
}
