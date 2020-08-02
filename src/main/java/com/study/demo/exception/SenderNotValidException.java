package com.study.demo.exception;

public class SenderNotValidException extends IllegalArgumentException {

    private static final long serialVersionUID = -8107546401589745784L;

    public static final String MESSAGE = "The remote address of the sender bank {%s} does not correspond to the Bank Code {%s} of the Sender Account";

    public SenderNotValidException(String senderIP, String bankCode) {
        super(String.format(MESSAGE, senderIP, bankCode));
    }

}
