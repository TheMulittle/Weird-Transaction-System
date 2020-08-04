package com.study.demo.exception;

public class AmountGreaterThanMaximumException extends IllegalArgumentException {

    private static final long serialVersionUID = 1L;

    public static final String MESSAGE = "The transaction amount {%s} is greater than the maximum allowed {%s}";

    public AmountGreaterThanMaximumException(long amount, Long maxAmount) {
        super(String.format(MESSAGE, amount, maxAmount));
    }

}
