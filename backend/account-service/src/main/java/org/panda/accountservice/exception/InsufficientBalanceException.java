package org.panda.accountservice.exception;

public class InsufficientBalanceException extends RuntimeException {
    public InsufficientBalanceException(String insufficientBalance) {
        super(insufficientBalance);
    }
}
