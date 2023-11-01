package com.cts.customer.controller;

import com.cts.account.model.AccountType;
import com.cts.customer.model.CreateCustomerResponse;
import com.cts.customer.model.Customer;
import com.cts.customer.model.CustomerDetailsResponse;
import com.cts.customer.model.CustomerProfileResponse;
import com.cts.customer.service.CustomerService;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;


import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.is;
 
@WebMvcTest(CustomerController.class)
public class CustomerControllerTest {

	@Autowired
    private MockMvc mockMvc;

    @InjectMocks
    private CustomerController customerController;

    @MockBean
    private CustomerService customerService;
    
    @Test
    public void testCreateCustomer() throws Exception {
      
        Customer customer = new Customer();
        customer.setCustomerName("John Doe");
        customer.setPassword("password123");
        
        CreateCustomerResponse response = new CreateCustomerResponse();
        response.setCustomerId(1001L);
        response.setMessage("Customer created successfully");

        when(customerService.createCustomer("mocked-token", customer)).thenReturn(response);

        mockMvc.perform(post("/api/customers/create-customer")
                .header("Authorization", "mocked-token")
                .contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testAllCustomersWithSavingsAccount() throws Exception {
       
        List<CustomerDetailsResponse> responseList = new ArrayList<>();
        CustomerDetailsResponse customer1 = new CustomerDetailsResponse();
        customer1.setAccountNo(12345L);
        customer1.setCustomerName("John Doe");
        customer1.setDateOfBirth(LocalDate.of(1990, 1, 1));
        customer1.setPan("ABCDE1234F");
        customer1.setAddress("123 Main St");
        customer1.setAccountType(AccountType.SAVINGS);
        customer1.setCurrentBalance(new BigDecimal("1000.00"));

        responseList.add(customer1);
        when(customerService.getAllCustomersWithSavingsAccount("mocked-token")).thenReturn(responseList);

        mockMvc.perform(get("/api/customers/allCustomers-with-savings-account")
                .header("Authorization", "mocked-token"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].accountNo", is(12345)))
                .andExpect(jsonPath("$[0].customerName", is("John Doe")))
                .andExpect(jsonPath("$[0].dateOfBirth", is("1990-01-01")))
                .andExpect(jsonPath("$[0].pan", is("ABCDE1234F")))
                .andExpect(jsonPath("$[0].address", is("123 Main St")))
                .andExpect(jsonPath("$[0].accountType", is("SAVINGS")))
                .andExpect(jsonPath("$[0].currentBalance", is(1000.00))); 
        
        verify(customerService).hasEmployeePermission("mocked-token");
        verify(customerService).getAllCustomersWithSavingsAccount("mocked-token");
    }
    
    @Test
    public void testSearchCustomer() throws Exception {
     
        CustomerDetailsResponse response = new CustomerDetailsResponse();
        response.setAccountNo(12345L);
        response.setCustomerName("John Doe");
        response.setDateOfBirth(LocalDate.of(1990, 1, 1));
        response.setPan("ABCDE1234F");
        response.setAddress("123 Main St");
        response.setAccountType(AccountType.SAVINGS);
        response.setCurrentBalance(new BigDecimal("1000.00"));

        when(customerService.searchCustomer("mocked-token", 12345L, AccountType.SAVINGS)).thenReturn(response);

        mockMvc.perform(get("/api/customers/search-customer/12345/SAVINGS")
                .header("Authorization", "mocked-token"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.accountNo", is(12345)))
                .andExpect(jsonPath("$.customerName", is("John Doe")))
                .andExpect(jsonPath("$.dateOfBirth", is("1990-01-01")))
                .andExpect(jsonPath("$.pan", is("ABCDE1234F")))
                .andExpect(jsonPath("$.address", is("123 Main St")))
                .andExpect(jsonPath("$.accountType", is("SAVINGS")))
                .andExpect(jsonPath("$.currentBalance", is(1000.00))); 
        
        verify(customerService).hasEmployeePermission("mocked-token");
        verify(customerService).searchCustomer("mocked-token", 12345L, AccountType.SAVINGS);
    }
    
    @Test
    public void testDeleteCustomer() throws Exception {
        
        when(customerService.deleteCustomer("mocked-token", 1001L)).thenReturn("Customer deleted successfully");

        mockMvc.perform(delete("/api/customers/delete-customer/1001")
                .header("Authorization", "mocked-token")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Customer deleted successfully")); 
        verify(customerService).hasEmployeePermission("mocked-token");
        verify(customerService).deleteCustomer("mocked-token", 1001L);
    }
    
    @Test
    public void testViewCustomer() throws Exception {
        
        List<CustomerProfileResponse> responseList = new ArrayList<>();
        CustomerProfileResponse customerProfile1 = new CustomerProfileResponse();
        customerProfile1.setAccountNo(12345L);
        customerProfile1.setName("John Doe");
        customerProfile1.setDob(LocalDate.of(1990, 1, 1));
        customerProfile1.setPan("ABCDE1234F");
        customerProfile1.setAddress("123 Main St");
        customerProfile1.setAccountType(AccountType.SAVINGS);
        customerProfile1.setCurrentBalance(new BigDecimal("1000.00"));

        responseList.add(customerProfile1);
        when(customerService.viewCustomer("mocked-token", 1001L)).thenReturn(responseList);

        mockMvc.perform(get("/api/customers/view-customer/1001")
                .header("Authorization", "mocked-token"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].accountNo", is(12345)))
                .andExpect(jsonPath("$[0].name", is("John Doe")))
                .andExpect(jsonPath("$[0].dob", is("1990-01-01")))
                .andExpect(jsonPath("$[0].pan", is("ABCDE1234F")))
                .andExpect(jsonPath("$[0].address", is("123 Main St")))
                .andExpect(jsonPath("$[0].accountType", is("SAVINGS")))
                .andExpect(jsonPath("$[0].currentBalance", is(1000.00))); 

        verify(customerService).viewCustomer("mocked-token", 1001L);
    }
}




