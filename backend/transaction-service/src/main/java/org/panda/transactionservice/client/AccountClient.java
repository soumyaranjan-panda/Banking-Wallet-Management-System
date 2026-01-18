package org.panda.transactionservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "account-service")
public interface AccountClient {
    @GetMapping("/account/{id}/balance")
    Double getBalance(@PathVariable("id") Long accountId, @RequestHeader("Authorization") String token
    );

    @PutMapping("/account/debit/{id}")
    void debit(@PathVariable("id") Long accountId, @RequestParam Double amount, @RequestHeader("Authorization") String token
    );

    @PutMapping("/account/credit/{id}")
    void credit(@PathVariable("id") Long accountId, @RequestParam Double amount, @RequestHeader("Authorization") String token
    );
}
