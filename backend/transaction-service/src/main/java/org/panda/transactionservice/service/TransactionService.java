package org.panda.transactionservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.panda.transactionservice.client.AccountClient;
import org.panda.transactionservice.dto.AccountResponse;
import org.panda.transactionservice.entity.*;
import org.panda.transactionservice.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionService {

    private final TransactionRepository repository;
    private final AccountClient accountClient;

    @Transactional
    public Transaction transfer(
            Long fromAccountId,
            Long toAccountId,
            Double amount,
            String token) {

        log.info("Transfer request: fromAccountId={}, toAccountId={}, amount={}", fromAccountId, toAccountId, amount);

        if (amount == null || amount <= 0) {
            log.error("Invalid transfer amount: {}", amount);
            throw new IllegalArgumentException("Transfer amount must be positive");
        }
        
        if (fromAccountId.equals(toAccountId)) {
            log.error("Attempted transfer to same account: {}", fromAccountId);
            throw new IllegalArgumentException("Cannot transfer to the same account");
        }
        
        boolean ownsFrom = accountClient.validateOwner(fromAccountId, token);
        if (!ownsFrom) {
            log.error("User does not own source account: {}", fromAccountId);
            throw new RuntimeException("User does not own the source account");
        }
        
        boolean toExists = accountClient.exists(toAccountId, token);
        if (!toExists) {
            log.error("Destination account does not exist: {}", toAccountId);
            throw new RuntimeException("Destination account does not exist");
        }

   
        Double balance = accountClient.getBalance(fromAccountId, token);
        if (balance < amount) {
            log.error("Insufficient balance: balance={}, amount={}", balance, amount);
            throw new RuntimeException("Insufficient balance");
        }

        try {
            accountClient.debit(fromAccountId, amount, token);
            accountClient.credit(toAccountId, amount, token);
            log.info("Transfer completed successfully");
        } catch (Exception e) {
            log.error("Transfer failed: {}", e.getMessage());
            Transaction tx = new Transaction();
            tx.setFromAccountId(fromAccountId);
            tx.setToAccountId(toAccountId);
            tx.setAmount(amount);
            tx.setType(TransactionType.TRANSFER);
            tx.setStatus(TransactionStatus.FAILED);
            tx.setCreatedAt(LocalDateTime.now());
            repository.save(tx);
            throw e;
        }

        Transaction tx = new Transaction();
        tx.setFromAccountId(fromAccountId);
        tx.setToAccountId(toAccountId);
        tx.setAmount(amount);
        tx.setType(TransactionType.TRANSFER);
        tx.setStatus(TransactionStatus.SUCCESS);
        tx.setCreatedAt(LocalDateTime.now());

        return repository.save(tx);
    }

    public List<Transaction> history(Long accountId) {
        log.debug("Fetching transaction history for account: {}", accountId);
        return repository.findByAccountIdOrderByCreatedAtDesc(accountId);
    }

    public List<Transaction> getAllTransactionsForUser(Long userId, String token) {
        log.info("Fetching all transactions for userId: {}", userId);

        List<AccountResponse> accounts = accountClient.getAllAccounts(token);
        List<Long> accountIds = accounts.stream()
                .map(AccountResponse::getId)
                .toList();
        
        if (accountIds.isEmpty()) {
            log.debug("No accounts found for userId: {}", userId);
            return List.of();
        }
        
        log.debug("Found {} accounts for userId: {}", accountIds.size(), userId);
        return repository.findByAccountIdsOrderByCreatedAtDesc(accountIds);
    }
}
