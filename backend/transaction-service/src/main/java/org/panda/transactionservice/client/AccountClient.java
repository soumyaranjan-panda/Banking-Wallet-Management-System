package org.panda.transactionservice.client;

import org.panda.transactionservice.dto.AccountResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "account-service")
public interface AccountClient {
    @GetMapping("/account/{id}/validate-owner")
    boolean validateOwner(
            @PathVariable("id") Long accountId,
            @RequestHeader("Authorization") String token
    );

    @GetMapping("/account/{id}/exists")
    boolean exists(
            @PathVariable Long id,
            @RequestHeader("Authorization") String token
    );

    @GetMapping("/account/{id}/balance")
    Double getBalance(@PathVariable("id") Long accountId, @RequestHeader("Authorization") String token
    );

    @PutMapping("/account/debit/{id}")
    void debit(@PathVariable("id") Long accountId, @RequestParam Double amount, @RequestHeader("Authorization") String token
    );

    @PutMapping("/account/credit/{id}")
    void credit(@PathVariable("id") Long accountId, @RequestParam Double amount, @RequestHeader("Authorization") String token
    );

    @GetMapping("/account")
    List<AccountResponse> getAllAccounts(@RequestHeader("Authorization") String token);
}
