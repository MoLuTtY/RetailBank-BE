package com.cts.rule.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.cts.rule.model.AccountType;
import com.cts.rule.model.RuleStatus;
import com.cts.rule.model.ServicechargeResponse;
import com.cts.rule.service.RuleService;


@WebMvcTest(RuleController.class)
public class RuleControllerTest {
	
	@Autowired
    private MockMvc mockMvc;

    @MockBean
    private RuleService ruleService;
    
    @Test
    public void testServiceCharge() throws Exception {
        
        ServicechargeResponse response = new ServicechargeResponse();
        response.setId(1L);
        response.setAccountNo(12345L);
        response.setAccountType(AccountType.SAVINGS);
        response.setDeductionDate(LocalDate.now());
        response.setServiceCharge(10.0f);
        response.setReference("Reference123");

        when(ruleService.serviceCharge("mocked-token", 12345L, AccountType.SAVINGS)).thenReturn(response);

        mockMvc.perform(get("/api/rules/service-charge/12345/SAVINGS")
                .header("Authorization", "mocked-token"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.accountNo", is(12345)))
                .andExpect(jsonPath("$.accountType", is("SAVINGS")))
                .andExpect(jsonPath("$.deductionDate").isNotEmpty())
                .andExpect(jsonPath("$.serviceCharge", is(10.0)))
                .andExpect(jsonPath("$.reference", is("Reference123"))); // Adjust the expected values

        verify(ruleService).serviceCharge("mocked-token", 12345L, AccountType.SAVINGS);
    }
    
    @Test
    public void testEvaluateMinBal() throws Exception {
       
        RuleStatus status = RuleStatus.ALLOWED;
        when(ruleService.evaluateMinBal("mocked-token", new BigDecimal("100.0"))).thenReturn(status);

        mockMvc.perform(get("/api/rules/evaluateMinBal/100.0")
                .header("Authorization", "mocked-token"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", is("ALLOWED"))); 
        verify(ruleService).evaluateMinBal("mocked-token", new BigDecimal("100.0"));
    }

}
