package com.cts.rule.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cts.rule.model.AccountType;
import com.cts.rule.model.ServicechargeResponse;

public interface ServicechargeRepository extends JpaRepository<ServicechargeResponse, Long> {

	@Query("SELECT s FROM ServicechargeResponse s WHERE s.accountNo = :accountNo AND s.accountType = :accountType ORDER BY s.deductionDate DESC")
    ServicechargeResponse findTopByAccountNoAndAccountTypeOrderByDeductionDateDesc(
        @Param("accountNo") Long accountNo, 
        @Param("accountType") AccountType accountType
    );
}
