package com.cts.customer.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateCustomerResponse {
    private Long customerId;
    private String message;

    public CreateCustomerResponse(Long customerId, String message) {
        this.customerId = customerId;
        this.message = message;
    }

}
