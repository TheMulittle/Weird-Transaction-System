package com.study.demo.exception;

public class SameEntityException extends IllegalArgumentException {

    private static final long serialVersionUID = 3594662330949641124L;

    public SameEntityException(String entityCode) {
        super("Both Receiver's entity code and sender's are the same: {" + entityCode + "}");
    }

}
