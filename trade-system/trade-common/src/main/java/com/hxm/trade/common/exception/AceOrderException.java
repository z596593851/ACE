package com.hxm.trade.common.exception;

public class AceOrderException extends RuntimeException {
    public AceOrderException(){

    }

    public AceOrderException(String message) {
        super(message);
    }

    public AceOrderException(Throwable cause) {
        super(cause);
    }
}
