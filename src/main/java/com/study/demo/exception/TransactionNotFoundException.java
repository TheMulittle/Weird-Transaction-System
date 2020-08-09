package com.study.demo.exception;

public class TransactionNotFoundException extends IllegalArgumentException {

    private static final long serialVersionUID = 1L;

    public TransactionNotFoundException(String transactionReference, String bankCode) {
        super("A transaction from bank [" + bankCode + "] with reference [" + transactionReference
                + "] does not exist");
    }

}
