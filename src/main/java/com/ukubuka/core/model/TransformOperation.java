package com.ukubuka.core.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ukubuka.core.exception.TransformException;

/**
 * Transform Operation
 * 
 * @author agrawroh
 * @version v1.0
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum TransformOperation {

    RENAME("rename"), DELETE("delete"), REMOVE("remove"), ADD("add"), NEW(
            "new"), MOVE("move"), SWAP("swap"), INCLUDE(
                    "include"), EXCLUDE("exclude"), NONE("no-operation");

    /* Operation */
    private String operation;

    /************** Private Constructor ***************/
    private TransformOperation(final String operation) {
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
    public static TransformOperation getOperation(final String source)
            throws TransformException {
        for (final TransformOperation transformOperation : TransformOperation
                .values()) {
            if (transformOperation.getOperation().equals(source)) {
                return transformOperation;
            }
        }
        throw new TransformException("Unsupported Operation!");
    }
}
