package org.panda.transactionservice.dto;

import lombok.Data;

@Data
public class DebitRequest {
    private Long accountId;
    private Double amount;
}
