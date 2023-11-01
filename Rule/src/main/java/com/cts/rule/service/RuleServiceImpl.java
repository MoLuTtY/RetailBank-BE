package com.cts.rule.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cts.rule.exception.AccessDeniedException;
import com.cts.rule.feignClient.AccountFeignClient;
import com.cts.rule.feignClient.AuthFeignClient;
import com.cts.rule.model.AccountType;
import com.cts.rule.model.AuthenticationResponse;
import com.cts.rule.model.RuleStatus;
import com.cts.rule.model.ServicechargeResponse;
import com.cts.rule.repository.ServicechargeRepository;


@Service
public class RuleServiceImpl implements RuleService{
	
	@Autowired
	private ServicechargeRepository servicechargeRepository;
	
	private final AccountFeignClient accountFeignClient;
	private final AuthFeignClient authFeignClient;

    @Autowired
    public RuleServiceImpl(AccountFeignClient accountFeignClient, AuthFeignClient authFeignClient) {
        this.accountFeignClient = accountFeignClient;
        this.authFeignClient = authFeignClient;
    }
    
    @Override
   	public AuthenticationResponse hasPermission(String token) {
   		return authFeignClient.getValidity(token);
   	}
       
       @Override
   	public AuthenticationResponse hasCustomerPermission(String token) {
   		AuthenticationResponse validity = authFeignClient.getValidity(token);
   		if (!authFeignClient.getRole(token, validity.getUserid()).equals("CUSTOMER"))
   			throw new AccessDeniedException("NOT ALLOWED");
   		else
   			return validity;
   	}
       
       @Override
   	public AuthenticationResponse hasEmployeePermission(String token) {
   		AuthenticationResponse validity = authFeignClient.getValidity(token);

   		if (!authFeignClient.getRole(token, validity.getUserid()).equals("EMPLOYEE"))
   			throw new AccessDeniedException("NOT ALLOWED");
   		else
   			return validity;
   	}

    
    @Override
    public ServicechargeResponse serviceCharge(String token, Long accountNo, AccountType accountType) {
        
        LocalDate currentDate = LocalDate.now();

        LocalDate oneMonthAgo = currentDate.minus(Period.ofMonths(1));

        ServicechargeResponse lastDeduction = servicechargeRepository.findTopByAccountNoAndAccountTypeOrderByDeductionDateDesc(accountNo, accountType);

        if (lastDeduction == null || lastDeduction.getDeductionDate().isBefore(oneMonthAgo)) {
            BigDecimal currentBalance = accountFeignClient.getCurrentBalance(token, accountNo, accountType);
            BigDecimal minimumBalance = new BigDecimal("1000.00");
            ServicechargeResponse response = new ServicechargeResponse();

            if (currentBalance.compareTo(minimumBalance) < 0) {
                BigDecimal amountToSubtract = new BigDecimal("100.00");
                response.setAccountNo(accountNo);
                response.setAccountType(accountType);
                response.setDeductionDate(currentDate); 
                response.setServiceCharge(100);
                response.setReference("Deducted");
                BigDecimal newBalance = currentBalance.subtract(amountToSubtract);
                accountFeignClient.updateCurrentBalance(token, accountNo, accountType, newBalance);
                servicechargeRepository.save(response);
            } else {
            	response.setAccountNo(accountNo);
                response.setAccountType(accountType);
                response.setDeductionDate(currentDate);
                response.setServiceCharge(0);
                response.setReference("No deduction required");
               
            }

            return response;
        } else {
             ServicechargeResponse response = new ServicechargeResponse();
             response.setAccountNo(accountNo);
             response.setAccountType(accountType);
             response.setDeductionDate(currentDate);
             response.setServiceCharge(0);
        	 response.setReference("Already deducted");

            return response;
        }
    }


	@Override
	public RuleStatus evaluateMinBal(String token,BigDecimal balance) {
		 BigDecimal minimumBalance = BigDecimal.valueOf(1000);
		 if (balance.compareTo(minimumBalance) >= 0) {
			 System.out.println("----------ALLOWED---------");
	         return RuleStatus.ALLOWED;
	         
	     } else {
	         return RuleStatus.DENIED;
	     }
		 
	}

}
