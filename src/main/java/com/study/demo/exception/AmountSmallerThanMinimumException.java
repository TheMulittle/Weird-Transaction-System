package com.study.demo.exception;

public class AmountSmallerThanMinimumException extends IllegalArgumentException {

    private static final long serialVersionUID = 1L;

    public static final String MESSAGE = "The transaction amount {%s} is smaller or equal to the minimum allowed {%s}";

    public AmountSmallerThanMinimumException(long amount, Long minAmount) {
        super(String.format(MESSAGE, amount, minAmount));
    }

}
