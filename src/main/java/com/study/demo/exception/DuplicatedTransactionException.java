package com.study.demo.exception;

public class DuplicatedTransactionException extends IllegalArgumentException {

    private static final long serialVersionUID = 1L;

    public DuplicatedTransactionException(String transactionReference, String bankCode) {
        super("A transaction from bank [" + bankCode + "] with reference [" + transactionReference
                + "] already exists");
    }

}
