package com.cts.customer.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cts.account.model.AccountType;
import com.cts.customer.model.CreateCustomerResponse;
import com.cts.customer.model.Customer;
import com.cts.customer.model.CustomerDetailsResponse;
import com.cts.customer.service.CustomerService;

@RestController
@RequestMapping("/api/customers")
@CrossOrigin
public class CustomerController {
	
    @Autowired
    private CustomerService customerService;
     
    @PostMapping("/create-customer")
    public ResponseEntity<CreateCustomerResponse> createCustomer(@RequestBody Customer customer) {
        CreateCustomerResponse response = customerService.createCustomer(customer);
        if (response.getCustomerId() != null) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    @GetMapping("/allCustomers-with-savings-account")
    public ResponseEntity<List<CustomerDetailsResponse>> allCustomersWithSavingsAccount() {
        List<CustomerDetailsResponse> result = customerService.getAllCustomersWithSavingsAccount();
        return ResponseEntity.ok(result);
    }
    
    @GetMapping("/search-customer/{accountNo}/{accountType}")
    public ResponseEntity<CustomerDetailsResponse> searchCustomer(@PathVariable Long accountNo, @PathVariable AccountType accountType) {

            CustomerDetailsResponse response = customerService.searchCustomer(accountNo, accountType);
            return ResponseEntity.ok(response);
        
    }
    
    @DeleteMapping("/delete-customer/{customerId}")
	public String deleteCustomer(@PathVariable Long customerId) {
		return customerService.deleteCustomer(customerId);
	}
        
}