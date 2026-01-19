package org.panda.transactionservice.dto;

import lombok.Data;

@Data
public class AccountResponse {
    private Long id;
    private Long userId;
    private Double balance;
    private boolean blocked;
}
