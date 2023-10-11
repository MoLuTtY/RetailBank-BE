package com.cts.rule.service;

import java.math.BigDecimal;

import com.cts.rule.model.AccountType;
import com.cts.rule.model.RuleStatus;
import com.cts.rule.model.ServicechargeResponse;

public interface RuleService {

	ServicechargeResponse serviceCharge(String token, Long accountNo, AccountType accountType);

	RuleStatus evaluateMinBal(String token,BigDecimal balance);

}
