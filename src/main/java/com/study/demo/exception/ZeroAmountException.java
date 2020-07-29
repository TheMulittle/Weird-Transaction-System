package com.study.demo.exception;

public class ZeroAmountException extends IllegalArgumentException {

    private static final long serialVersionUID = 1L;

    public ZeroAmountException() {
        super("A transaction cannot have amount set to zero");
    }

}
