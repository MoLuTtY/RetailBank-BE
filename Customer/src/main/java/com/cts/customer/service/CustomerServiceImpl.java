package com.cts.customer.service;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cts.account.model.Account;
import com.cts.account.model.AccountType;
import com.cts.customer.exception.AccessDeniedException;
import com.cts.customer.exception.AccountNotFoundException;
import com.cts.customer.exception.CustomerNotFoundException;
import com.cts.customer.feignClient.AccountFeignClient;
import com.cts.customer.feignClient.AuthFeignClient;
import com.cts.customer.feignClient.TransactionFeignClient;
import com.cts.customer.model.AppUser;
import com.cts.customer.model.AuthenticationResponse;
import com.cts.customer.model.CreateCustomerResponse;
import com.cts.customer.model.Customer;
import com.cts.customer.model.CustomerDetailsResponse;
import com.cts.customer.model.CustomerProfileResponse;
import com.cts.customer.repository.CustomerRepository;

import feign.FeignException;

@Service
public class CustomerServiceImpl implements CustomerService{
	
	@Autowired
	private CustomerRepository customerRepository;
	
	private final AccountFeignClient accountFeignClient;
	private final TransactionFeignClient transactionFeignClient; 
	private final AuthFeignClient authFeignClient;
	
    @Autowired
    public CustomerServiceImpl(AccountFeignClient accountFeignClient, TransactionFeignClient transactionFeignClient, AuthFeignClient authFeignClient) {
        this.accountFeignClient = accountFeignClient;
		this.transactionFeignClient = transactionFeignClient;
		this.authFeignClient = authFeignClient;
    }
    
    @Override
	public AuthenticationResponse hasPermission(String token) {
		return authFeignClient.getValidity(token);
	}
    
    @Override
	public AuthenticationResponse hasCustomerPermission(String token) {
		AuthenticationResponse validity = authFeignClient.getValidity(token);
		if (!authFeignClient.getRole(validity.getUserid()).equals("CUSTOMER"))
			throw new AccessDeniedException("NOT ALLOWED");
		else
			return validity;
	}
    
    @Override
	public AuthenticationResponse hasEmployeePermission(String token) {
		AuthenticationResponse validity = authFeignClient.getValidity(token);
		if (!authFeignClient.getRole(validity.getUserid()).equals("EMPLOYEE"))
			throw new AccessDeniedException("NOT ALLOWED");
		else
			return validity;
	}
	
	public Long generateCustomerId(Customer customer) {
      
        String panDigits = customer.getPan().substring(5, 9);
        
        String dobYear = customer.getDateOfBirth().format(DateTimeFormatter.ofPattern("yy"));
        String dobMonth = customer.getDateOfBirth().format(DateTimeFormatter.ofPattern("MM"));
        Long yearMonthSum = Long.parseLong(dobYear)+Long.parseLong(dobMonth);

        String combinedIdString = yearMonthSum+panDigits + dobYear;
        Long generatedCustomerId = Long.parseLong(combinedIdString);

        return generatedCustomerId;
    }

	public CreateCustomerResponse createCustomer(Customer customer) {
		if (customer.getCustomerName() == null || customer.getPassword() == null ||
	            customer.getDateOfBirth() == null || customer.getPan() == null ||
	            customer.getAddress() == null) {
	            return new CreateCustomerResponse(null, "All fields must be provided");
	        }
        try {

        	customer.setCustomerId(generateCustomerId(customer));
        	Customer createdCustomer = customerRepository.save(customer);
        	Long customerId = createdCustomer.getCustomerId(); 
            String message = "Customer created successfully";
            

            AppUser user = new AppUser(Long.toString(customer.getCustomerId()), Long.toString(customer.getCustomerId()), customer.getPassword(), null,
					"CUSTOMER");
            authFeignClient.createUser(user);
            
            return new CreateCustomerResponse(customerId, message);
        } catch (Exception e) {
           
            return new CreateCustomerResponse(null, "Error creating customer");
        }
       
        
    }

	public List<CustomerDetailsResponse> getAllCustomersWithSavingsAccount() {
		
        List<CustomerDetailsResponse> result = new ArrayList<>();

        List<Customer> customers = customerRepository.findAll();

        for (Customer customer : customers) {
            Long customerId = customer.getCustomerId();

            Account account = accountFeignClient.getCustomerSavingsAccount(customerId);

            if (account != null) {
                CustomerDetailsResponse customerDetails = new CustomerDetailsResponse();
                customerDetails.setAccountNo(account.getAccountId().getAccountNo());
                customerDetails.setCustomerName(customer.getCustomerName());
                customerDetails.setPan(customer.getPan());
                customerDetails.setDateOfBirth(customer.getDateOfBirth());
                customerDetails.setAddress(customer.getAddress());
                customerDetails.setAccountType(account.getAccountId().getAccountType());
                customerDetails.setCurrentBalance(account.getCurrentBalance());

                result.add(customerDetails);
            }
        }

        return result;
		
    }

	@Override
	public CustomerDetailsResponse searchCustomer(Long accountNo, AccountType accountType) {
	    try {
	        Account account = accountFeignClient.viewCustomerByAccountNoAndAccountType(accountNo, accountType);

	        if (account != null) {
	            Customer customer = customerRepository.searchCustomer(account.getCustomerId());
	            CustomerDetailsResponse customerDetails = new CustomerDetailsResponse();
	            
	            customerDetails.setAccountNo(account.getAccountId().getAccountNo());
	            customerDetails.setAccountType(account.getAccountId().getAccountType());
	            customerDetails.setCustomerName(customer.getCustomerName());
	            customerDetails.setDateOfBirth(customer.getDateOfBirth());
	            customerDetails.setPan(customer.getPan());
	            customerDetails.setAddress(customer.getAddress());
	            customerDetails.setCurrentBalance(account.getCurrentBalance());
	            
	            return customerDetails;
	        } else {
	            throw new AccountNotFoundException("Account not found for accountNo: " + accountNo);
	        }
	    } catch (FeignException.NotFound e) {	       
	        throw new AccountNotFoundException("Account not found for accountNo: " + accountNo);
	    }
	}

	@Override
	public String deleteCustomer(Long customerId) {
		Optional<Customer> customer = customerRepository.findById(customerId);
		if(!customer.isPresent()) {
			throw new CustomerNotFoundException("Customer doesn't exist");
		}
		else {
		
			Account account = accountFeignClient.getCustomerSavingsAccount(customerId);
			transactionFeignClient.deleteTransactions(account.getAccountId().getAccountNo());
			accountFeignClient.deleteAllAccountsByAccountNo(account.getAccountId().getAccountNo());
			authFeignClient.deleteUser(Long.toString(customerId));
		
			customerRepository.deleteById(customerId);
			return "Customer with id "+customerId+" deleted successfully";
		}
	}

	@Override
	public List<CustomerProfileResponse> viewCustomer(Long customerId) {
		Customer customer = customerRepository.getById(customerId);
		List<CustomerProfileResponse> result = new ArrayList<>();
		
		Account savingsAccount = accountFeignClient.getCustomerSavingsAccount(customerId);
		Account currentAccount = accountFeignClient.findCurrentAccounts(customerId);
		
	    CustomerProfileResponse savingsResponse = new CustomerProfileResponse();
	    savingsResponse.setName(customer.getCustomerName());	
	    savingsResponse.setDob(customer.getDateOfBirth());
	    savingsResponse.setPan(customer.getPan());
	    savingsResponse.setAddress(customer.getAddress());
	    savingsResponse.setAccountNo(savingsAccount.getAccountId().getAccountNo());
	    savingsResponse.setAccountType(savingsAccount.getAccountId().getAccountType());
	    savingsResponse.setCurrentBalance(savingsAccount.getCurrentBalance());
	    
	    result.add(savingsResponse);
	    
	    CustomerProfileResponse currentResponse = new CustomerProfileResponse();
	    currentResponse.setName(customer.getCustomerName());	
	    currentResponse.setDob(customer.getDateOfBirth());
	    currentResponse.setPan(customer.getPan());
	    currentResponse.setAddress(customer.getAddress());
	    currentResponse.setAccountNo(currentAccount.getAccountId().getAccountNo());
	    currentResponse.setAccountType(currentAccount.getAccountId().getAccountType());
	    currentResponse.setCurrentBalance(currentAccount.getCurrentBalance());
	    
	    result.add(currentResponse);
	    
	    return result;
	    
	}		
}
