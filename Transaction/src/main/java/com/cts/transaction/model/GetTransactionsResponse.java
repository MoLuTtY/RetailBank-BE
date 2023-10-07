package com.cts.transaction.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetTransactionsResponse {
	
	private LocalDate transactionDate;
    private AccountType sourceAccountType;
    private Long targetAccountNo;
    private BigDecimal withdrawalAmount;
    private BigDecimal transferAmount;  
    private BigDecimal depositAmount;
    private BigDecimal closingBalance;

}
