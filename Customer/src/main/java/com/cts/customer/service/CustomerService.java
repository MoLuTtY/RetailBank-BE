package com.cts.customer.service;

import java.util.List;

//import javax.security.auth.login.AccountNotFoundException;

import com.cts.account.model.AccountType;
import com.cts.customer.exception.AccountNotFoundException;
import com.cts.customer.model.CreateCustomerResponse;
import com.cts.customer.model.Customer;
import com.cts.customer.model.CustomerDetailsResponse;


public interface CustomerService {

	CreateCustomerResponse createCustomer(Customer customer);
	List<CustomerDetailsResponse> getAllCustomersWithSavingsAccount();
	CustomerDetailsResponse searchCustomer(Long accountNo, AccountType accountType) throws AccountNotFoundException;
	String deleteCustomer(Long customerId);
	

}