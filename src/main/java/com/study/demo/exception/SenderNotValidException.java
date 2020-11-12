package com.study.demo.exception;

public class SenderNotValidException extends IllegalArgumentException {

    private static final long serialVersionUID = -8107546401589745784L;

    public static final String MESSAGE = "The remote address of the sender entity {%s} does not correspond to the Entity Code {%s} of the Sender Account";

    public SenderNotValidException(String senderIP, String entityCode) {
        super(String.format(MESSAGE, senderIP, entityCode));
    }

}
