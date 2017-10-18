package com.ukubuka.core.exception;

/**
 * Reader Exception
 * 
 * @author agrawroh
 * @version v1.0
 */
public class ReaderException extends Exception {

    private static final long serialVersionUID = -3926176543832150497L;

    /************************* Constructor *************************/
    public ReaderException(String message) {
        super(message);
    }

    public ReaderException(Throwable cause) {
        super(cause);
    }

    public ReaderException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReaderException(String message, Throwable cause,
            boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}