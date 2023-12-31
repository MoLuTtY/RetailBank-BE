package com.cts.account.model;

import java.io.Serializable;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccountId implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long accountNo;

    @NotNull(message = "Account type cannot be null")
    @Enumerated(EnumType.STRING)
    private AccountType accountType;
}
