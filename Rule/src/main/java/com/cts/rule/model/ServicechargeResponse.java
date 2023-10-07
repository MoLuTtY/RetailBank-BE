package com.cts.rule.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@Entity
public class ServicechargeResponse {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@JsonProperty("accountNo")
	private Long accountNo;
	@JsonProperty("accountType")
	private AccountType accountType;
	@JsonProperty("deductionDate")
	private LocalDate deductionDate;
	
    @JsonProperty("serviceCharge")
    private float serviceCharge;
    
    @JsonProperty("reference")
    private String reference;

}

