package com.ukubuka.core.transform;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ukubuka.core.evaluator.UkubukaExpressionEvaluator;
import com.ukubuka.core.exception.TransformException;
import com.ukubuka.core.model.FileRecord;
import com.ukubuka.core.model.UkubukaSchema.Operations;

/**
 * Ukubuka Transformer
 * 
 * @author yashvardhannanavati
 * @version v1.0
 */
@Component
public class UkubukaTransformer {

    /************************* Dependency Injections *************************/
    @Autowired
    private UkubukaExpressionEvaluator expressionEvaluator;

    /**
     * Perform Operations
     * 
     * @param fileHeader
     * @param operationsList
     * @param fileRecords
     * @throws TransformException
     */
    public void performOperations(List<String> fileHeader,
            List<FileRecord> fileRecords, List<Operations> operationsList)
            throws TransformException {
        // @ToDo
    }
}
