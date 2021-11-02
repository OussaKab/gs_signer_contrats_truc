package com.repository;

import com.model.Contract;

import java.util.List;

public interface ContractRepository extends JpaRepository<Contract, Long> {

    List<Contract> findAllByManagerSignatureNull();
}
