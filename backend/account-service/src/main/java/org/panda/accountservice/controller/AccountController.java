package org.panda.accountservice.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.panda.accountservice.entity.Account;
import org.panda.accountservice.exception.AccountNotFoundException;
import org.panda.accountservice.service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
@Slf4j
public class AccountController {

    private final AccountService accountService;

    @PostMapping("/create")
    public ResponseEntity<Account> createAccount(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) {
            log.error("UserId not found in JWT token");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        log.info("Creating account for user: {}", userId);
        Account account = accountService.createAccount(userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(account);
    }

    @PutMapping("/debit/{accountId}")
    public ResponseEntity<Void> debit(
            @PathVariable Long accountId, 
            @RequestParam @NotNull @Min(value = 0, message = "Amount must be positive") Double amount, 
            HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        log.info("Debit request: accountId={}, userId={}, amount={}", accountId, userId, amount);
        accountService.debit(accountId, userId, amount);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/credit/{accountId}")
    public ResponseEntity<Void> credit(
            @PathVariable Long accountId, 
            @RequestParam @NotNull @Min(value = 0, message = "Amount must be positive") Double amount) {
        log.info("Credit request: accountId={}, amount={}", accountId, amount);
        accountService.credit(accountId, amount);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{accountId}/balance")
    public ResponseEntity<Double> getBalance(@PathVariable Long accountId, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) {
            log.error("UserId not found in JWT token");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        log.debug("Balance request: accountId={}, userId={}", accountId, userId);
        Double balance = accountService.getAccount(accountId, userId).getBalance();
        return ResponseEntity.ok(balance);
    }

    @GetMapping
    public ResponseEntity<List<Account>> getAllAccounts(HttpServletRequest request){
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) {
            log.error("UserId not found in JWT token");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        log.debug("Get all accounts request for userId: {}", userId);
        List<Account> accounts = accountService.getAllAccounts(userId);
        return ResponseEntity.ok(accounts);
    }

    @GetMapping("/{accountId}/validate-owner")
    public ResponseEntity<Boolean> validateOwner(
            @PathVariable Long accountId,
            HttpServletRequest request) {

        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.ok(false);
        }
        boolean isOwner = accountService.isOwner(accountId, userId);
        return ResponseEntity.ok(isOwner);
    }

    @GetMapping("/{id}/exists")
    public ResponseEntity<Boolean> exists(@PathVariable Long id) {
        boolean exists = accountService.existsById(id);
        return ResponseEntity.ok(exists);
    }

    @PutMapping("/block/{accountId}")
    public ResponseEntity<Void> blockAccount(@PathVariable Long accountId) {
        log.info("Block account request: accountId={}", accountId);
        accountService.blockAccount(accountId);
        return ResponseEntity.ok().build();
    }
}
