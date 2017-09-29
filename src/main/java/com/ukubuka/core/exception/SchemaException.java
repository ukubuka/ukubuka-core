package com.ukubuka.core.exception;

/**
 * Schema Exception
 * 
 * @author agrawroh
 * @version v1.0
 */
public class SchemaException extends Exception {

    private static final long serialVersionUID = 5614522984072357837L;

    /************************* Constructor *************************/
    public SchemaException(String message) {
        super(message);
    }

    public SchemaException(Throwable cause) {
        super(cause);
    }

    public SchemaException(String message, Throwable cause) {
        super(message, cause);
    }

    public SchemaException(String message, Throwable cause,
            boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}