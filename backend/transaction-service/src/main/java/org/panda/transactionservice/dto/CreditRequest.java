package org.panda.transactionservice.dto;

import lombok.Data;

@Data
public class CreditRequest {
    private Long accountId;
    private Double amount;
}
