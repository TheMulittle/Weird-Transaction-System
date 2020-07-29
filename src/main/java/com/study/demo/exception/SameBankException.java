package com.study.demo.exception;

public class SameBankException extends IllegalArgumentException {

    private static final long serialVersionUID = 3594662330949641124L;

    public SameBankException(String bankCode) {
        super("Both Receiver's bank code and sender's are the same: [" + bankCode + "]");
    }

}
