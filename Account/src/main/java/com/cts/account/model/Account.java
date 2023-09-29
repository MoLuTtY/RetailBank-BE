package com.cts.account.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name="ACCOUNT")
public class Account {
	@Id
	@Column(name="account_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
    private AccountId accountId;
    private Long customerId;
    @NotNull(message="Opening amount cannot be null")
    private BigDecimal openingAmount;
    private BigDecimal currentBalance;
    @NotNull(message="Initiation date cannot be null")
    private LocalDate initiationDate;
    
}

