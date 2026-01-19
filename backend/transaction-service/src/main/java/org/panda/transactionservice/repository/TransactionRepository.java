package org.panda.transactionservice.repository;

import org.panda.transactionservice.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByFromAccountIdOrderByCreatedAtDesc(Long accountId);

    List<Transaction> findByToAccountIdOrderByCreatedAtDesc(Long accountId);
    
    @Query("SELECT t FROM Transaction t WHERE t.fromAccountId = :accountId OR t.toAccountId = :accountId ORDER BY t.createdAt DESC")
    List<Transaction> findByAccountIdOrderByCreatedAtDesc(@Param("accountId") Long accountId);
    
    @Query("SELECT t FROM Transaction t WHERE t.fromAccountId IN :accountIds OR t.toAccountId IN :accountIds ORDER BY t.createdAt DESC")
    List<Transaction> findByAccountIdsOrderByCreatedAtDesc(@Param("accountIds") List<Long> accountIds);
}
