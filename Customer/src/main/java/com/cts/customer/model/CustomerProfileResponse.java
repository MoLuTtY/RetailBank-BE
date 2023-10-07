package com.cts.customer.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.cts.account.model.AccountType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerProfileResponse {
	
	private Long accountNo;
	private String name;
	private LocalDate dob;
	private String pan;
	private String address;
	private AccountType accountType;
	private BigDecimal currentBalance;

}
