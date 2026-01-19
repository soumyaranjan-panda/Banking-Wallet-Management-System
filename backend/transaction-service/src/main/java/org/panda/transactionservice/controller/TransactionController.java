package org.panda.transactionservice.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.panda.transactionservice.dto.TransferRequest;
import org.panda.transactionservice.entity.Transaction;
import org.panda.transactionservice.service.TransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transaction")
@RequiredArgsConstructor
@Slf4j
public class TransactionController {
    private final TransactionService service;

    @PostMapping("/transfer")
    public ResponseEntity<Transaction> transfer(@RequestBody TransferRequest request, HttpServletRequest httpRequest) {
        String token = httpRequest.getHeader("Authorization");
        Transaction transaction = service.transfer(request.getFromAccountId(), request.getToAccountId(), request.getAmount(), token);
        return ResponseEntity.ok(transaction);
    }

    @GetMapping("/history")
    public ResponseEntity<List<Transaction>> getAllTransactions(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) {
            log.error("UserId not found in JWT token");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String token = request.getHeader("Authorization");
        List<Transaction> transactions = service.getAllTransactionsForUser(userId, token);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/history/{accountId}")
    public ResponseEntity<List<Transaction>> history(@PathVariable Long accountId) {
        List<Transaction> transactions = service.history(accountId);
        return ResponseEntity.ok(transactions);
    }
}
