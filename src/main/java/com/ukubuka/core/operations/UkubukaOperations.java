package com.ukubuka.core.operations;

import java.util.Map;

import com.ukubuka.core.exception.PipelineException;
import com.ukubuka.core.model.FileContents;
import com.ukubuka.core.model.UkubukaSchema;

/**
 * Ukubuka Operations
 * 
 * @author agrawroh
 * @version v1.0
 */
@FunctionalInterface
public interface UkubukaOperations {

    /**
     * Perform Operations
     * 
     * @param dataFiles
     * @param schema
     * @throws PipelineException
     */
    void performOperations(Map<String, FileContents> dataFiles,
            final UkubukaSchema schema) throws PipelineException;
}