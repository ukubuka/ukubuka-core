package com.ukubuka.core.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ukubuka.core.exception.TransformException;

/**
 * Load Operation
 * 
 * @author agrawroh
 * @version v1.0
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum LoadOperation {

    JOIN("join"), DISTINCT("distinct");

    /* Operation */
    private String operation;

    /********** Private Constructor **********/
    private LoadOperation(final String operation) {
        this.operation = operation;
    }

    /**
     * @return the operation
     */
    public String getOperation() {
        return operation;
    }

    /**
     * Get Operation
     * 
     * @param operation
     * @return TransformOperation
     * @throws TransformException
     */
    public static LoadOperation getOperation(final String source)
            throws TransformException {
        for (final LoadOperation transformOperation : LoadOperation.values()) {
            if (transformOperation.getOperation().equals(source)) {
                return transformOperation;
            }
        }
        throw new TransformException("Unsupported Operation!");
    }
}
