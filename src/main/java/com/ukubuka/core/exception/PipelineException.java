package com.ukubuka.core.exception;

/**
 * Pipeline Exception
 * 
 * @author agrawroh
 * @version v1.0
 */
public class PipelineException extends Exception {

    private static final long serialVersionUID = 9004489108873423681L;

    /************************* Constructor *************************/
    public PipelineException(String message) {
        super(message);
    }

    public PipelineException(Throwable cause) {
        super(cause);
    }

    public PipelineException(String message, Throwable cause) {
        super(message, cause);
    }

    public PipelineException(String message, Throwable cause,
            boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}