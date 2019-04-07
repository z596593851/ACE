package com.hxm.trade.common.exception;

public class AceMQException extends Exception {
    private static final long serialVersionUID = -8842749869325361816L;

    public AceMQException() {
        super();
    }

    public AceMQException(String message) {
        super(message);
    }

    public AceMQException(String message, Throwable cause) {
        super(message, cause);
    }

    public AceMQException(Throwable cause) {
        super(cause);
    }

    protected AceMQException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
