package com.cts.account.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.cts.account.model.Account;
import com.cts.account.model.AccountId;
import com.cts.account.model.AccountType;
import com.cts.account.service.AccountService;
import com.fasterxml.jackson.databind.ObjectMapper;


@WebMvcTest(AccountController.class)
public class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    @Test
    public void testCreateAccount() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/accounts/create-account")
                .header("Authorization", "your-auth-token")        
                .contentType(MediaType.APPLICATION_JSON));

    }
     
    @Test
    public void testFindSavingsAccounts() throws Exception {
       
        Long customerId = 123L;

        AccountId accountId = new AccountId(1L, AccountType.SAVINGS); 
        Account sampleAccount = new Account();
        sampleAccount.setId(1L);
        sampleAccount.setAccountId(accountId);
        sampleAccount.setCustomerId(customerId);
        sampleAccount.setOpeningAmount(new BigDecimal("1000.0"));
        sampleAccount.setCurrentBalance(new BigDecimal("1000.0")); 
        sampleAccount.setInitiationDate(LocalDate.now()); 

        when(accountService.findSavingsAccounts(anyString(), eq(customerId))).thenReturn(sampleAccount);
        
        mockMvc.perform(get("/api/accounts/find-savings-accounts/{customerId}", customerId)
                .header("Authorization", "your-auth-token"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(sampleAccount.getId()))
                .andExpect(jsonPath("$.accountId.accountNo").value(accountId.getAccountNo()))
                .andExpect(jsonPath("$.accountId.accountType").value(accountId.getAccountType().toString()))
                .andExpect(jsonPath("$.customerId").value(sampleAccount.getCustomerId()))
                .andExpect(jsonPath("$.openingAmount").value(sampleAccount.getOpeningAmount().toString()))
                .andExpect(jsonPath("$.currentBalance").value(sampleAccount.getCurrentBalance().toString()))
                .andExpect(jsonPath("$.initiationDate").value(sampleAccount.getInitiationDate().toString()));

        verify(accountService, times(1)).findSavingsAccounts(anyString(), eq(customerId));
    }
    
    @Test
    public void testFindCurrentAccounts() throws Exception {
      
        Long customerId = 123L;

        AccountId accountId = new AccountId(1L, AccountType.CURRENT); 
        Account sampleAccount = new Account();
        sampleAccount.setId(1L);
        sampleAccount.setAccountId(accountId);
        sampleAccount.setCustomerId(customerId);
        sampleAccount.setOpeningAmount(new BigDecimal("1000.0")); 
        sampleAccount.setCurrentBalance(new BigDecimal("1000.0"));
        sampleAccount.setInitiationDate(LocalDate.now()); 

        when(accountService.findCurrentAccounts(anyString(), eq(customerId))).thenReturn(sampleAccount);

        mockMvc.perform(get("/api/accounts/find-current-accounts/{customerId}", customerId)
                .header("Authorization", "your-auth-token"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(sampleAccount.getId()))
                .andExpect(jsonPath("$.accountId.accountNo").value(accountId.getAccountNo()))
                .andExpect(jsonPath("$.accountId.accountType").value(accountId.getAccountType().toString()))
                .andExpect(jsonPath("$.customerId").value(sampleAccount.getCustomerId()))
                .andExpect(jsonPath("$.openingAmount").value(sampleAccount.getOpeningAmount().toString()))
                .andExpect(jsonPath("$.currentBalance").value(sampleAccount.getCurrentBalance().toString()))
                .andExpect(jsonPath("$.initiationDate").value(sampleAccount.getInitiationDate().toString()));
       
        verify(accountService, times(1)).findCurrentAccounts(anyString(), eq(customerId));
    }
    
    @Test
    public void testViewCustomerByAccountNoAndAccountType() throws Exception {
       
        Account sampleAccount = new Account();
        sampleAccount.setId(1L);
        sampleAccount.setAccountId(new AccountId(12345L, AccountType.SAVINGS));
        sampleAccount.setCustomerId(1001L);
        sampleAccount.setOpeningAmount(new BigDecimal("1000.00"));
        sampleAccount.setCurrentBalance(new BigDecimal("1050.00"));
        sampleAccount.setInitiationDate(LocalDate.now());

        when(accountService.viewCustomerByAccountNoAndAccountType("mocked-token", 12345L, AccountType.SAVINGS)).thenReturn(sampleAccount);

        mockMvc.perform(get("/api/accounts/viewCustomer-by-accountNo-and-accountType/12345/SAVINGS")
                .header("Authorization", "mocked-token"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.accountId.accountNo", is(12345)))
                .andExpect(jsonPath("$.accountId.accountType", is("SAVINGS")))
                .andExpect(jsonPath("$.customerId", is(1001)))
                .andExpect(jsonPath("$.openingAmount", is(1000.00)))
                .andExpect(jsonPath("$.currentBalance", is(1050.00)))
                .andExpect(jsonPath("$.initiationDate", notNullValue()));
    }
    
    @Test
    public void testDeleteAllAccountsByAccountNo() throws Exception {
    
        doNothing().when(accountService).deleteAllAccountsByAccountNo("mocked-token", 12345L);

        mockMvc.perform(delete("/api/accounts/delete-account/12345")
                .header("Authorization", "mocked-token"))
                .andExpect(status().isOk());

        verify(accountService).hasEmployeePermission("mocked-token");
        verify(accountService).deleteAllAccountsByAccountNo("mocked-token", 12345L);
    }
    
    @Test
    public void testGetCustomerIdByAccountNo() throws Exception {
      
        when(accountService.getgetCustomerIdByAccountNo("mocked-token", 12345L)).thenReturn(1001L);

        mockMvc.perform(get("/api/accounts/get-customerId/12345")
                .header("Authorization", "mocked-token"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string("1001")); 
    }
    
    @Test
    public void testDeposit() throws Exception {
        
        mockMvc.perform(post("/api/accounts/deposit/12345/SAVINGS/100.00")
                .header("Authorization", "mocked-token")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Status : Deposit Successful")); 

        verify(accountService).hasEmployeePermission("mocked-token");
        verify(accountService).deposit("mocked-token", 12345L, AccountType.SAVINGS, new BigDecimal("100.00"));
    }
    
    @Test
    public void testGetCurrentBalance() throws Exception {
    	
        BigDecimal expectedBalance = new BigDecimal("1050.00");
        when(accountService.getCurrentBalance("mocked-token", 12345L, AccountType.SAVINGS)).thenReturn(expectedBalance);

        mockMvc.perform(get("/api/accounts/current-balance/12345/SAVINGS")
                .header("Authorization", "mocked-token"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(expectedBalance.toString()));
    }
    
    @Test
    public void testUpdateCurrentBalance() throws Exception {
     
        doNothing().when(accountService).updateCurrentBalance("mocked-token", 12345L, AccountType.SAVINGS, new BigDecimal("1500.00"));

        mockMvc.perform(put("/api/accounts/update-currentBalance/12345/SAVINGS/1500.00")
                .header("Authorization", "mocked-token")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(accountService).updateCurrentBalance("mocked-token", 12345L, AccountType.SAVINGS, new BigDecimal("1500.00"));
    }
    
    @Test
    public void testWithdraw() throws Exception {
       
        mockMvc.perform(post("/api/accounts/withdraw/12345/SAVINGS/100.00")
                .header("Authorization", "mocked-token")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Status : Withdrawal Successful"));
        
      
        verify(accountService).hasCustomerPermission("mocked-token");
        verify(accountService).withdraw("mocked-token", 12345L, AccountType.SAVINGS, new BigDecimal("100.00"));
    }
    
    @Test
    public void testGetAccount() throws Exception {
        
        Long expectedAccountId = 12345L;
        when(accountService.getAccount("mocked-token", 12345L, AccountType.SAVINGS)).thenReturn(expectedAccountId);

        mockMvc.perform(get("/api/accounts/getAccount/12345/SAVINGS")
                .header("Authorization", "mocked-token"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(expectedAccountId.toString()));
    }

    @SuppressWarnings("unused")
	private String asJsonString(Object obj) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
