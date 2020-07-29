package com.study.demo.exception;

public class AmountGreaterThanMaximumException extends IllegalArgumentException {

    private static final long serialVersionUID = 1L;

    public AmountGreaterThanMaximumException(long amount, long maxAmount) {
        super("The transaction had amount [" + amount + "] which is greater than the maximum amount allowed: ["
                + maxAmount + "]");
    }

}
