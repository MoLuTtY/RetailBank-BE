package com.cts.rule.controller;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cts.rule.model.AccountType;
import com.cts.rule.model.RuleStatus;
import com.cts.rule.model.ServicechargeResponse;
import com.cts.rule.service.RuleService;

@RestController
@RequestMapping("/api/rules")
@CrossOrigin
public class RuleController {
	
	@Autowired
	private RuleService ruleService;
	
	@GetMapping(value = "/service-charge/{accountNo}/{accountType}", produces = "application/json")
	public ResponseEntity<ServicechargeResponse> serviceCharge(@PathVariable Long accountNo, @PathVariable AccountType accountType) {
		ServicechargeResponse response = ruleService.serviceCharge(accountNo, accountType);
	    return ResponseEntity.ok(response);
	}
	
	@GetMapping(value = "/evaluateMinBal/{balance}", produces = "application/json")
	public ResponseEntity<RuleStatus> evaluateMinBal(@PathVariable BigDecimal balance) {
		RuleStatus status = ruleService.evaluateMinBal(balance);
		return ResponseEntity.ok(status);
	}
}
