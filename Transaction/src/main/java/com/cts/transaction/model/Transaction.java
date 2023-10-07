package com.cts.transaction.model;

import java.math.BigDecimal;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name="TRANSACTIONS")
public class Transaction {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id")
    private Long transactionId;
	
	@NotNull(message="Transaction date cannot be null")
	@Column(name = "transaction_date")
    private Date transactionDate;

    @NotNull(message="Account id cannot be null")
    private Long accountId;
    
    @NotNull(message="Customer id cannot be null")
    private Long customerId;

    private Long sourceAccountNo;

    @Enumerated(EnumType.STRING)
    private AccountType sourceAccountType;

    private Long targetAccountNo;

    private BigDecimal withdrawalAmount;

    private BigDecimal transferAmount;
    
    private BigDecimal depositAmount;

    @NotNull(message="Closing balance cannot be null")
    private BigDecimal closingBalance;

}
