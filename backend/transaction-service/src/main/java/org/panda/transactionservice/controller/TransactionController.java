package org.panda.transactionservice.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.panda.transactionservice.dto.TransferRequest;
import org.panda.transactionservice.entity.Transaction;
import org.panda.transactionservice.service.TransactionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transaction")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService service;

    @PostMapping("/transfer")
    public Transaction transfer(@RequestBody TransferRequest request, HttpServletRequest httpRequest) {
        String token = httpRequest.getHeader("Authorization");
        return service.transfer(request.getFromAccountId(), request.getToAccountId(), request.getAmount(), token);
    }

    @GetMapping("/history/{accountId}")
    public List<Transaction> history(@PathVariable Long accountId) {
        return service.history(accountId);
    }
}
