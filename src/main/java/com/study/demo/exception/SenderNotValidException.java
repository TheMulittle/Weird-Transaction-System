package com.study.demo.exception;

public class SenderNotValidException extends IllegalArgumentException {

    private static final long serialVersionUID = 2L;

    public SenderNotValidException(String senderIP, String bankCode) {
        super("The given pair senderIP/bankCode is not valid: [" + senderIP + "/" + bankCode + "]");
    }

}
