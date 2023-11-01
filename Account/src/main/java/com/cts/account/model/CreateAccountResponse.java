package com.cts.account.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateAccountResponse {
    private Long accountNo;
    private String message;

    public CreateAccountResponse(Long accountNo, String message) {
        this.accountNo = accountNo;
        this.message = message;
    }

}
