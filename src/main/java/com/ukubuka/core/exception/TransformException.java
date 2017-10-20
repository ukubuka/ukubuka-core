package com.ukubuka.core.exception;

/**
 * Transform Exception
 * 
 * @author agrawroh
 * @version v1.0
 */
public class TransformException extends Exception {

    private static final long serialVersionUID = 7567668318371591962L;

    /************************* Constructor *************************/
    public TransformException(String message) {
        super(message);
    }

    public TransformException(Throwable cause) {
        super(cause);
    }

    public TransformException(String message, Throwable cause) {
        super(message, cause);
    }

    public TransformException(String message, Throwable cause,
            boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}