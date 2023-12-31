package com.cts.customer.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.cts.account.model.AccountType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDetailsResponse {
	
	private Long accountNo;
    private String customerName;
    private LocalDate dateOfBirth;
    private String pan;
    private String address;
    private AccountType accountType;
    private BigDecimal currentBalance;

}
