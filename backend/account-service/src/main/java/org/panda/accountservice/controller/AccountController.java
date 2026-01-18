package org.panda.accountservice.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.panda.accountservice.entity.Account;
import org.panda.accountservice.service.AccountService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping("/create")
    public Account createAccount(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        System.out.println(userId);
        if (userId == null) {
            throw new RuntimeException("UserId not found in JWT");
        }
        return accountService.createAccount(userId);
    }

    @PutMapping("/debit/{accountId}")
    public void debit(@PathVariable Long accountId, @RequestParam Double amount, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        accountService.debit(accountId, userId, amount);
    }

    @PutMapping("/credit/{accountId}")
    public void credit(@PathVariable Long accountId, @RequestParam Double amount) {
        accountService.credit(accountId, amount);
    }


    @GetMapping("/{accountId}/balance")
    public Double getBalance(@PathVariable Long accountId, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) {
            throw new RuntimeException("UserId not found in JWT");
        }
        System.out.println(accountId + " " + userId);
        return accountService.getAccount(accountId, userId).getBalance();
    }

    @PutMapping("/block/{accountId}")
    public void blockAccount(@PathVariable Long accountId) {
        accountService.blockAccount(accountId);
    }
}
