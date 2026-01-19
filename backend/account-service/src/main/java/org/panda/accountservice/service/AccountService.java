package org.panda.accountservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.panda.accountservice.entity.Account;
import org.panda.accountservice.exception.AccountBlockedException;
import org.panda.accountservice.exception.AccountNotFoundException;
import org.panda.accountservice.exception.InsufficientBalanceException;
import org.panda.accountservice.repository.AccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountService {

    private final AccountRepository accountRepository;

    public Account createAccount(Long userId) {
        log.info("Creating account for userId: {}", userId);
        Account account = new Account();
        account.setUserId(userId);
        account.setBalance(0.0);
        account.setBlocked(false);
        Account savedAccount = accountRepository.save(account);
        log.info("Account created successfully: accountId={}, userId={}", savedAccount.getId(), userId);
        return savedAccount;
    }

    @Transactional
    public void debit(Long accountId, Long userId, Double amount) {
        log.info("Debit operation: accountId={}, userId={}, amount={}", accountId, userId, amount);
        
        if (amount == null || amount <= 0) {
            log.error("Invalid debit amount: {}", amount);
            throw new IllegalArgumentException("Debit amount must be positive");
        }
        
        Account acc = getAccount(accountId, userId);

        if (acc.getBalance() < amount) {
            log.error("Insufficient balance: balance={}, amount={}", acc.getBalance(), amount);
            throw new InsufficientBalanceException("Insufficient balance");
        }

        acc.setBalance(acc.getBalance() - amount);
        accountRepository.save(acc);
        log.info("Debit successful: new balance={}", acc.getBalance());
    }

    @Transactional
    public void credit(Long accountId, Double amount) {
        log.info("Credit operation: accountId={}, amount={}", accountId, amount);
        
        if (amount == null || amount <= 0) {
            log.error("Invalid credit amount: {}", amount);
            throw new IllegalArgumentException("Credit amount must be positive");
        }
        
        Account acc = accountRepository.findById(accountId)
                .orElseThrow(() -> {
                    log.error("Account not found: {}", accountId);
                    return new AccountNotFoundException("Account not found");
                });

        if (acc.isBlocked()) {
            log.error("Attempted credit to blocked account: {}", accountId);
            throw new AccountBlockedException("Cannot credit a blocked account");
        }

        acc.setBalance(acc.getBalance() + amount);
        accountRepository.save(acc);
        log.info("Credit successful: new balance={}", acc.getBalance());
    }


    public Account getAccount(Long accountId, Long userId) {

        Account account = accountRepository.findByIdAndUserId(accountId, userId).orElseThrow(() -> new AccountNotFoundException("Account not found"));

        if (account.isBlocked()) {
            throw new AccountBlockedException("Account is blocked");
        }

        return account;
    }

    public boolean isOwner(Long accountId, Long userId) {
        return accountRepository
                .findByIdAndUserId(accountId, userId)
                .isPresent();
    }


    public void blockAccount(Long accountId) {
        log.info("Blocking account: accountId={}", accountId);
        
        Account account = accountRepository
                .findById(accountId)
                .orElseThrow(() -> {
                    log.error("Account not found for blocking: {}", accountId);
                    return new AccountNotFoundException("Account not found with id " + accountId);
                });

        account.setBlocked(true);
        accountRepository.save(account);
        log.info("Account blocked successfully: accountId={}", accountId);
    }

    public boolean existsById(Long id) {
        return accountRepository.existsById(id);
    }

    public List<Account> getAllAccounts(Long userId) {
        return accountRepository.findAllByUserId(userId);
    }
}
