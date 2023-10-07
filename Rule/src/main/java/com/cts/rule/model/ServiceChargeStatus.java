package com.cts.rule.model;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class ServiceChargeStatus {
	
	@JsonProperty("accountNo")
	private Long accountNo;
	@JsonProperty("accountType")
	private AccountType accountType;	
    @JsonProperty("serviceCharge")
    private float serviceCharge;

    @JsonProperty("reference")
    private String reference;
    

}
