package org.panda.transactionservice.service;

import lombok.RequiredArgsConstructor;
import org.panda.transactionservice.client.AccountClient;
import org.panda.transactionservice.entity.*;
import org.panda.transactionservice.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository repository;
    private final AccountClient accountClient;

    @Transactional
    public Transaction transfer(Long fromAccountId, Long toAccountId, Double amount, String token) {
        Double balance = accountClient.getBalance(fromAccountId, token);

        if (balance < amount) {
            throw new RuntimeException("Insufficient balance");
        }

        accountClient.debit(fromAccountId, amount, token);
        accountClient.credit(toAccountId, amount, token);

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
        List<Transaction> list = repository.findByFromAccountId(accountId);
        list.addAll(repository.findByToAccountId(accountId));
        return list;
    }
}
