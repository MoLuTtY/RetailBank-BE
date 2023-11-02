package com.cts.account.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.cts.account.model.Account;
import com.cts.account.model.AccountType;

public interface AccountRepository extends JpaRepository<Account, Long>{

	Account findTopByOrderByAccountIdDesc();
	
    @Query("SELECT a FROM Account a WHERE a.customerId = :customerId AND a.accountId.accountType = 'SAVINGS'")
    Account findSavingsAccountByCustomerId(@Param("customerId") Long customerId);

    @Query("SELECT a FROM Account a WHERE a.accountId.accountNo = :accountNo AND a.accountId.accountType = :accountType")
	Account viewCustomerByAccountNoAndAccountType(@Param("accountNo") Long accountNo, @Param("accountType") AccountType accountType);

    @Modifying
    @Transactional
    @Query("DELETE FROM Account a WHERE a.accountId.accountNo = :accountNo")
    void deleteAllByAccountIdAccountNo(Long accountNo);

    @Query("SELECT a FROM Account a WHERE a.accountId.accountNo = :accountNo AND a.accountId.accountType = 'SAVINGS'" )
	Account getCustomerId(@Param("accountNo") Long accountNo);

    @Query("SELECT a FROM Account a WHERE a.accountId.accountNo = :accountNo AND a.accountId.accountType = :accountType")
	Account getAccountByNoAndType(@Param("accountNo") Long accountNo,@Param("accountType") AccountType accountType);

    @Query("SELECT a FROM Account a WHERE a.customerId = :customerId AND a.accountId.accountType = 'CURRENT'")
	Account findCurrentAccountByCustomerId(@Param("customerId")Long customerId);
    
    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END FROM Account a WHERE a.accountId.accountNo = :accountNo AND a.accountId.accountType = :accountType")
    boolean doesAccountExist(@Param("accountNo")Long accountNo, @Param("accountType")AccountType accountType);


}
