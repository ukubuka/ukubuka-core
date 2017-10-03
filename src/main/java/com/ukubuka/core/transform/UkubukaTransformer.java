package com.ukubuka.core.transform;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ukubuka.core.evaluator.UkubukaExpressionEvaluator;
import com.ukubuka.core.exception.TransformException;
import com.ukubuka.core.model.FileRecord;
import com.ukubuka.core.model.TransformOperation;
import com.ukubuka.core.model.UkubukaSchema.Operations;
import com.ukubuka.core.utilities.Constants;

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
        /* Iterate Operations */
        for (final Operations operation : operationsList) {
            /* Get Source & Target Values */
            String source = fileHeader.get(0).indexOf(
                    Constants.COLUMN_ENCOLSING_QUOTE) >= 0 ? Constants.COLUMN_ENCOLSING_QUOTE
                    + operation.getSource() + Constants.COLUMN_ENCOLSING_QUOTE
                    : operation.getSource();
            String target = Constants.COLUMN_ENCOLSING_QUOTE
                    + operation.getTarget() + Constants.COLUMN_ENCOLSING_QUOTE;

            /* Check Whether Column Exists */
            if (operation.getType() != TransformOperation.ADD
                    && !fileHeader.contains(source)) {
                throw new TransformException("Column Not Found!");
            }

            /* Perform Operation */
            performOperation(fileHeader, fileRecords, operation.getType(),
                    source, target);
        }
    }

    /**
     * Perform Operation
     * 
     * @param fileHeader
     * @param source
     * @param target
     * @param operationType
     * @param fileRecords
     * @throws TransformException
     */
    private void performOperation(List<String> fileHeader,
            List<FileRecord> fileRecords,
            final TransformOperation operationType, final String source,
            final String target) throws TransformException {
        switch (operationType) {
        /* Column Rename Operation */
            case RENAME:
                doRename(fileHeader, source, target);
                break;

            /* Column Delete Operation */
            case DELETE:
                doDelete(fileHeader, fileRecords, source);
                break;

            /* Column Add Operation */
            case ADD:
                doAdd(fileHeader, fileRecords, source, target);
                break;

            /* Unsupported Operation */
            default:
                throw new TransformException("Operation Not Supported!");
        }
    }

    /**
     * Perform Rename Operation
     * 
     * @param fileHeader
     * @param source
     * @param target
     */
    private void doRename(List<String> fileHeader, final String source,
            final String target) {
        /* Rename New Header */
        fileHeader.set(fileHeader.indexOf(source), target);
    }

    /**
     * Perform Delete Operation
     * 
     * @param columnNames
     * @param source
     * @param rowData
     */
    private void doDelete(List<String> fileHeader,
            List<FileRecord> fileRecords, final String source) {
        /* Get Index */
        int index = fileHeader.indexOf(source);

        /* Remove Header Entry */
        fileHeader.remove(index);

        /* Remove Records */
        for (final FileRecord fileRecord : fileRecords) {
            fileRecord.getData().remove(index);
        }
    }

    /**
     * Perform Add Operation
     * 
     * @param fileHeader
     * @param source
     * @param target
     * @param fileRecords
     */
    private void doAdd(List<String> fileHeader, List<FileRecord> fileRecords,
            final String source, final String target) {
        /* Add Source */
        fileHeader.add(source);

        /* Add New Column Values */
        for (final FileRecord fileRecord : fileRecords) {
            fileRecord.getData().add(
                    String.valueOf(expressionEvaluator.evaluate(fileRecord,
                            target)));
        }
    }
}
