package com.study.demo.exception;

public class TransactionNotFoundException extends IllegalArgumentException {

    private static final long serialVersionUID = 1L;

    public static final String MESSAGE = "A transaction from bank with code {%s} with reference {%s} was not found";

    public TransactionNotFoundException(String transactionReference, String bankCode) {
        super(String.format(MESSAGE, bankCode, transactionReference));
    }

}
