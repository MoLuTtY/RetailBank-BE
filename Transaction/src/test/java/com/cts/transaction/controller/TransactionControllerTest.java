package com.cts.transaction.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.cts.transaction.model.AccountType;
import com.cts.transaction.model.GetTransactionsResponse;
import com.cts.transaction.model.TransactionStatus;
import com.cts.transaction.service.TransactionService;

@WebMvcTest(TransactionController.class)
public class TransactionControllerTest {
	

	@Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionService transactionService;
    
    @Test
    public void testGetTransactions() throws Exception {
      
        List<GetTransactionsResponse> responseList = new ArrayList<>();
        
        GetTransactionsResponse response1 = new GetTransactionsResponse();
        response1.setTransactionDate(LocalDate.now());
        response1.setSourceAccountType(AccountType.SAVINGS);
        response1.setTargetAccountNo(1002L);
        response1.setWithdrawalAmount(new BigDecimal("50.0"));
        response1.setTransferAmount(new BigDecimal("0.0"));
        response1.setDepositAmount(new BigDecimal("100.0"));
        response1.setClosingBalance(new BigDecimal("150.0"));
        
        GetTransactionsResponse response2 = new GetTransactionsResponse();
      
        responseList.add(response1);
        responseList.add(response2);

        when(transactionService.getTransactions("mocked-token", 1001L)).thenReturn(responseList);

        mockMvc.perform(get("/api/transactions/get-transactions/1001")
                .header("Authorization", "mocked-token"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].transactionDate").isNotEmpty())
                .andExpect(jsonPath("$[0].sourceAccountType", is("SAVINGS")))
                .andExpect(jsonPath("$[0].targetAccountNo", is(1002)))
                .andExpect(jsonPath("$[0].withdrawalAmount", is(50.0)))
                .andExpect(jsonPath("$[0].transferAmount", is(0.0)))
                .andExpect(jsonPath("$[0].depositAmount", is(100.0)))
                .andExpect(jsonPath("$[0].closingBalance", is(150.0)));

        verify(transactionService).getTransactions("mocked-token", 1001L);
    }
    
    @Test
    public void testDeleteTransactions() throws Exception {
       
        doNothing().when(transactionService).deleteTransactions("mocked-token", 12345L);

        mockMvc.perform(delete("/api/transactions/delete-transactions/12345")
                .header("Authorization", "mocked-token"))
                .andExpect(status().isOk());

        verify(transactionService).deleteTransactions("mocked-token", 12345L);
    }
    

    @Test
    public void testDeposit() throws Exception {
        
        mockMvc.perform(post("/api/transactions/deposit")
                .header("Authorization", "mocked-token")
                .contentType(MediaType.APPLICATION_JSON));
    }
    
    @Test
    public void testGetTransactionsByDateRange() throws Exception {
      
        List<GetTransactionsResponse> responseList = new ArrayList<>();
        
        GetTransactionsResponse response1 = new GetTransactionsResponse();
        response1.setTransactionDate(LocalDate.now());
        response1.setSourceAccountType(AccountType.SAVINGS);
        response1.setTargetAccountNo(1002L);
        response1.setWithdrawalAmount(new BigDecimal("50.0"));
        response1.setTransferAmount(new BigDecimal("0.0"));
        response1.setDepositAmount(new BigDecimal("100.0"));
        response1.setClosingBalance(new BigDecimal("150.0"));
        
        GetTransactionsResponse response2 = new GetTransactionsResponse();
       
        responseList.add(response1);
        responseList.add(response2);

        when(transactionService.getTransactionsByDateRange("mocked-token", 1001L, "2023-01-01", "2023-12-31")).thenReturn(responseList);

        mockMvc.perform(get("/api/transactions/get-transactions/1001/2023-01-01/2023-12-31")
                .header("Authorization", "mocked-token"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].transactionDate").isNotEmpty())
                .andExpect(jsonPath("$[0].sourceAccountType", is("SAVINGS")))
                .andExpect(jsonPath("$[0].targetAccountNo", is(1002)))
                .andExpect(jsonPath("$[0].withdrawalAmount", is(50.0)))
                .andExpect(jsonPath("$[0].transferAmount", is(0.0)))
                .andExpect(jsonPath("$[0].depositAmount", is(100.0)))
                .andExpect(jsonPath("$[0].closingBalance", is(150.0)));

        verify(transactionService).getTransactionsByDateRange("mocked-token", 1001L, "2023-01-01", "2023-12-31");
    }
    
    @Test
    public void testTransfer() throws Exception {
       
        Long sourceAccountNo = 1001L;
        AccountType sourceAccountType = AccountType.SAVINGS;
        Long targetAccountNo = 1002L;
        BigDecimal amount = new BigDecimal("50.0");

        TransactionStatus status = new TransactionStatus();
        status.setTransactionId(1L);
        status.setStatus("Withdrawal successful");

        when(transactionService.trasfer("mocked-token", sourceAccountNo, sourceAccountType, targetAccountNo, amount)).thenReturn(status);

        mockMvc.perform(post("/api/transactions/transfer/1001/SAVINGS/1002/50.0")
                .header("Authorization", "mocked-token"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("transactionId", is(1)))
                .andExpect(jsonPath("status", is("Withdrawal successful")));

        verify(transactionService).hasCustomerPermission("mocked-token");
        verify(transactionService).trasfer("mocked-token", sourceAccountNo, sourceAccountType, targetAccountNo, amount);
    }
    

}
