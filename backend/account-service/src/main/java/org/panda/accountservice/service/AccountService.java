package org.panda.accountservice.service;

import lombok.RequiredArgsConstructor;
import org.panda.accountservice.entity.Account;
import org.panda.accountservice.exception.AccountBlockedException;
import org.panda.accountservice.exception.AccountNotFoundException;
import org.panda.accountservice.exception.InsufficientBalanceException;
import org.panda.accountservice.repository.AccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    public Account createAccount(Long userId) {
        Account account = new Account();
        account.setUserId(userId);
        account.setBalance(0.0);
        account.setBlocked(false);
        return accountRepository.save(account);
    }

    @Transactional
    public void debit(Long accountId, Long userId, Double amount) {
        Account acc = getAccount(accountId, userId);

        if (acc.getBalance() < amount) {
            throw new InsufficientBalanceException("Insufficient balance");
        }

        acc.setBalance(acc.getBalance() - amount);
        accountRepository.save(acc);
    }

    @Transactional
    public void credit(Long accountId, Double amount) {
        Account acc = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Account not found"));

        acc.setBalance(acc.getBalance() + amount);
        accountRepository.save(acc);
    }


    public Account getAccount(Long accountId, Long userId) {

        Account account = accountRepository.findByIdAndUserId(accountId, userId).orElseThrow(() -> new AccountNotFoundException("Account not found"));

        if (account.isBlocked()) {
            throw new AccountBlockedException("Account is blocked");
        }

        return account;
    }

    public void blockAccount(Long accountId) {

        Account account = accountRepository
                .findById(accountId)
                .orElseThrow(() ->
                        new AccountNotFoundException("Account not found with id " + accountId));

        account.setBlocked(true);
        accountRepository.save(account);
    }
}
