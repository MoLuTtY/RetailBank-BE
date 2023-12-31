package com.cts.customer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cts.customer.model.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long>{

	@Query("SELECT c FROM Customer c WHERE c.customerId = :customerId")
	Customer searchCustomer(@Param("customerId") Long customerId);

}
