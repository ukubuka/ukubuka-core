package com.ukubuka.core.transform;

import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ukubuka.core.evaluator.UkubukaExpressionEvaluator;
import com.ukubuka.core.exception.TransformException;
import com.ukubuka.core.model.FileRecord;
import com.ukubuka.core.model.TransformOperation;
import com.ukubuka.core.model.UkubukaSchema.TransformOperations;
import com.ukubuka.core.utilities.Constants;

/**
 * Ukubuka Transformer
 * 
 * @author yashvardhannanavati
 * @version v1.0
 */
@Component
public class UkubukaTransformer {

    /************************************ Logger Instance ***********************************/
    private static final Logger LOGGER = LoggerFactory
            .getLogger(UkubukaTransformer.class);

    /******************************** Dependency Injections *********************************/
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
            List<FileRecord> fileRecords,
            List<TransformOperations> operationsList) throws TransformException {
        /* Iterate Operations */
        for (final TransformOperations operation : operationsList) {
            LOGGER.info("Performing Transform: HC" + operation.hashCode());

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
                throw new TransformException("Column Not Found! Name: "
                        + source + " | Header: " + fileHeader);
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

            /* Column Delete Operation */
            case REMOVE:
                doDelete(fileHeader, fileRecords, source);
                break;

            /* Column Add Operation */
            case ADD:
                doAdd(fileHeader, fileRecords, source, target);
                break;

            /* Column Add Operation */
            case NEW:
                doAdd(fileHeader, fileRecords, source, target);
                break;

            /* Column Move Operation */
            case MOVE:
                doMove(fileHeader, fileRecords, source, target);
                break;

            /* Column Swap Operation */
            case SWAP:
                doSwap(fileHeader, fileRecords, source, target);
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
        LOGGER.info("Performing Rename Operation - Source: " + source
                + " | Target: " + target + " | Header: " + fileHeader);

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
        LOGGER.info("Performing Delete Operation - Source: " + source
                + " | Header: " + fileHeader);

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
        LOGGER.info("Performing Add Operation - Source: " + source
                + " | Target: " + target + " | Header: " + fileHeader);

        /* Add Source */
        fileHeader.add(source);

        /* Add New Column Values */
        for (final FileRecord fileRecord : fileRecords) {
            String expressionValue = String.valueOf(expressionEvaluator
                    .evaluate(fileRecord, target));
            LOGGER.info("Evaluated Expression Value: " + expressionValue);
            fileRecord.getData().add(expressionValue);
        }
    }

    /**
     * Perform Move Operation
     * 
     * @param fileHeader
     * @param source
     * @param target
     * @param fileRecords
     */
    private void doMove(List<String> fileHeader, List<FileRecord> fileRecords,
            final String source, final String target) {
        LOGGER.info("Performing Move Operation - Source: " + source
                + " | Target: " + target + " | Header: " + fileHeader);

        /* Get Source & Target Indices */
        int sourceIndex = fileHeader.indexOf(source);
        int targetIndex = Integer.parseInt(target.replace(
                Constants.COLUMN_ENCOLSING_QUOTE, Constants.EMPTY_STRING));

        /* Move Columns */
        String header = fileHeader.remove(sourceIndex);
        fileHeader.add(targetIndex, header);

        /* Move Data Column Values */
        for (final FileRecord fileRecord : fileRecords) {
            String data = fileRecord.getData().remove(sourceIndex);
            fileRecord.getData().add(targetIndex, data);
        }
    }

    /**
     * Perform Swap Operation
     * 
     * @param fileHeader
     * @param source
     * @param target
     * @param fileRecords
     */
    private void doSwap(List<String> fileHeader, List<FileRecord> fileRecords,
            final String source, final String target) {
        LOGGER.info("Performing Swap Operation - Source: " + source
                + " | Target: " + target + " | Header: " + fileHeader);

        /* Get Source & Target Indices */
        int sourceIndex = fileHeader.indexOf(source);
        int targetIndex = fileHeader.indexOf(target);

        /* Swap Columns */
        Collections.swap(fileHeader, sourceIndex, targetIndex);

        /* Swap Data Column Values */
        for (final FileRecord fileRecord : fileRecords) {
            Collections.swap(fileRecord.getData(), sourceIndex, targetIndex);
        }
    }
}
