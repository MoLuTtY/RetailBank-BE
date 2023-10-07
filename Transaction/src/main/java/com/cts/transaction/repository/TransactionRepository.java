package com.cts.transaction.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cts.transaction.model.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long>{

	@Query("SELECT t FROM Transaction t WHERE t.customerId = :customerId ORDER BY t.transactionId DESC")
    List<Transaction> getTransactionsByCustomerId(@Param("customerId") Long customerId);
}
