package com.ukubuka.core.exception;

/**
 * Writer Exception
 * 
 * @author agrawroh
 * @version v1.0
 */
public class WriterException extends Exception {

    private static final long serialVersionUID = -8083271170520479114L;

    /************************* Constructor *************************/
    public WriterException(String message) {
        super(message);
    }

    public WriterException(Throwable cause) {
        super(cause);
    }

    public WriterException(String message, Throwable cause) {
        super(message, cause);
    }

    public WriterException(String message, Throwable cause,
            boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}