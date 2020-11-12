package com.study.demo.exception;

public class TransactionNotFoundException extends IllegalArgumentException {

    private static final long serialVersionUID = 1L;

    public static final String MESSAGE = "A transaction from entity with code {%s} with reference {%s} was not found";

    public TransactionNotFoundException(String transactionReference, String entityCode) {
        super(String.format(MESSAGE, entityCode, transactionReference));
    }

}
