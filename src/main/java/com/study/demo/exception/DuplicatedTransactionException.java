package com.study.demo.exception;

public class DuplicatedTransactionException extends IllegalArgumentException {

    private static final long serialVersionUID = 6618158287081380053L;

    public static final String MESSAGE = "A transaction from entity with code {%s} with reference {%s} already exists";

    public DuplicatedTransactionException(String transactionReference, String entityCode) {
        super(String.format(MESSAGE, entityCode, transactionReference));
    }

}
