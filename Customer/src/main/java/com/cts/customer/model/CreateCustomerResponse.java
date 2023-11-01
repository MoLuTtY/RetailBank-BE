package com.cts.customer.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateCustomerResponse {
    private Long customerId;
    private String message;

    public CreateCustomerResponse(Long customerId, String message) {
        this.customerId = customerId;
        this.message = message;
    }

}
