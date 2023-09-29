package com.cts.account.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
public class CreateAccountRequest {
	private Long customerId;
    private BigDecimal openingAmount;
    private LocalDate initiationDate;

}
