package com.ukubuka.core.exception;

/**
 * Parser Exception
 * 
 * @author agrawroh
 * @version v1.0
 */
public class ParserException extends Exception {

    private static final long serialVersionUID = 5614522984072357837L;

    /************************* Constructor *************************/
    public ParserException(String message) {
        super(message);
    }

    public ParserException(Throwable cause) {
        super(cause);
    }

    public ParserException(String message, Throwable cause) {
        super(message, cause);
    }

    public ParserException(String message, Throwable cause,
            boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}