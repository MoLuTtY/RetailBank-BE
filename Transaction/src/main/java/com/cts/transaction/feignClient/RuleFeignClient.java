package com.cts.transaction.feignClient;

import java.math.BigDecimal;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import com.cts.transaction.model.RuleStatus;


@FeignClient(name = "rule-service", url = "http://localhost:8060")
public interface RuleFeignClient {

    @GetMapping("/api/rules/evaluateMinBal/{balance}")
    RuleStatus evaluateMinBal(@RequestHeader("Authorization") String token,@PathVariable BigDecimal balance);
}