package com.study.demo.exception;

public class IpAdressNotKnownException extends IllegalArgumentException {

    private static final long serialVersionUID = -8107546401589745784L;

    public static final String MESSAGE = "The remote address of the requestor is not known";

    public IpAdressNotKnownException() {
        super(MESSAGE);
    }

}
