package com.ukubuka.core.operations.transform;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.ukubuka.core.evaluator.UkubukaExpressionEvaluator;
import com.ukubuka.core.exception.ReaderException;
import com.ukubuka.core.exception.TransformException;
import com.ukubuka.core.model.FileContents;
import com.ukubuka.core.model.FileRecord;
import com.ukubuka.core.model.SupportedSource;
import com.ukubuka.core.model.TransformOperation;
import com.ukubuka.core.model.UkubukaSchema.Transform;
import com.ukubuka.core.model.UkubukaSchema.TransformOperations;
import com.ukubuka.core.model.UkubukaSchema.TransformOperationsType;
import com.ukubuka.core.reader.UkubukaReader;
import com.ukubuka.core.utilities.Constants;

/**
 * Ukubuka Transformer
 * 
 * @author yashvardhannanavati
 * @version v1.0
 * 
 * @author agrawroh
 * @version v1.1
 */
@Component
public class UkubukaTransformer {

    /************************************ Logger Instance ***********************************/
    private static final Logger LOGGER = LoggerFactory
            .getLogger(UkubukaTransformer.class);

    /************************************ Global Variables **********************************/
    private Map<String, String> sMap;
    private static final EnumSet<TransformOperation> TRANSFORM_OPERATION_EXCEPTIONS = EnumSet
            .of(TransformOperation.ADD, TransformOperation.NEW,
                    TransformOperation.INCLUDE, TransformOperation.EXCLUDE);

    /******************************** Dependency Injections *********************************/
    @Autowired
    private UkubukaExpressionEvaluator expressionEvaluator;

    @Autowired
    private UkubukaReader reader;

    /*********************************** Post Construct *************************************/
    @PostConstruct
    public void initShortcutMap() throws ReaderException {
        /* Get Mappings File */
        String[] fileContents;
        try {
            fileContents = this.reader
                    .readFileAsString(SupportedSource.FILE,
                            this.getClass().getClassLoader()
                                    .getResource("shortcut-mappings").getFile(),
                            Constants.DEFAULT_FILE_ENCODING)
                    .split(Constants.DEFAULT_FILE_END_LINE_DELIMITER);
        } catch (Exception ex) {
            LOGGER.error("Unable To Read Shortcut Mappings!", ex);
            throw new ReaderException("Unable To Read Shortcut Mappings!");
        }

        /* Create Shortcuts Map */
        sMap = new HashMap<>();
        for (final String fileContent : fileContents) {
            String[] keyValuePair = fileContent
                    .split(Constants.SHORTCUT_MAP_DELIMITER);
            sMap.put(keyValuePair[0], keyValuePair[1]);
        }
    }

    /**
     * Perform Transformations
     * 
     * @param dataFiles
     * @param transforms
     * @throws TransformException
     */
    public void performOperations(Map<String, FileContents> dataFiles,
            final List<Transform> transforms) throws TransformException {
        /* Get File Transformation */
        for (final Entry<String, FileContents> dataFile : dataFiles
                .entrySet()) {
            TransformOperationsType fileTransforms = getFileTransformationDetails(
                    dataFile.getKey(), transforms);

            /* Process File Transforms */
            if (null != fileTransforms) {
                /* Perform Column Operations */
                performTypeOperations(dataFile.getValue(),
                        fileTransforms.getColumn());

                /* Perform Row Operations */
                if (null != fileTransforms.getRow()) {
                    performTypeOperations(dataFile.getValue(),
                            Arrays.asList(fileTransforms.getRow()));
                }
            }
        }
    }

    /**
     * Perform Type Operations
     * 
     * @param fileContents
     * @param transformOperations
     * @throws TransformException
     */
    private void performTypeOperations(FileContents fileContents,
            final List<TransformOperations> transformOperations)
            throws TransformException {
        if (!CollectionUtils.isEmpty(transformOperations)) {
            LOGGER.info("Transform Count: #{}", transformOperations.size());
            performTransformOperations(fileContents.getHeader(),
                    fileContents.getData(), transformOperations);
        }
    }

    /**
     * Get File Transformation Details
     * 
     * @param fileId
     * @param transforms
     * @return Transform Operations Type
     */
    private TransformOperationsType getFileTransformationDetails(
            final String fileId, List<Transform> transforms) {
        /* Iterate Transforms */
        for (final Transform transform : transforms) {
            if (transform.getId().equals(fileId)) {
                return transform.getOperations();
            }
        }
        return null;
    }

    /**
     * Perform Operations
     * 
     * @param fileHeader
     * @param operationsList
     * @param fileRecords
     * @throws TransformException
     */
    private void performTransformOperations(List<String> fileHeader,
            List<FileRecord> fileRecords,
            List<TransformOperations> operationsList)
            throws TransformException {
        /* Iterate Operations */
        for (final TransformOperations operation : operationsList) {
            LOGGER.info("Performing Transform: HC{}", operation.hashCode());

            /* Check Whether Column Exists */
            String source = operation.getSource();
            if (!TRANSFORM_OPERATION_EXCEPTIONS.contains(operation.getType())
                    && !fileHeader.contains(source)) {
                throw new TransformException("Column Not Found! Name: " + source
                        + " | Header: " + fileHeader);
            }

            /* Perform Operation */
            performTransformOperation(fileHeader, fileRecords,
                    operation.getType(), source, operation.getTarget());
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
    private void performTransformOperation(List<String> fileHeader,
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

            /* Column Include Operation */
            case INCLUDE:
                doInclude(fileHeader, fileRecords, target);
                break;

            /* Column Exclude Operation */
            case EXCLUDE:
                doExclude(fileHeader, fileRecords, target);
                break;

            /* Unsupported Operation */
            default:
                throw new TransformException(
                        "Unsupported Operation: " + operationType);
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
        LOGGER.info(
                "Performing Rename Operation - Source: {} | Target: {} | Header: {}",
                source, target, fileHeader);

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
    private void doDelete(List<String> fileHeader, List<FileRecord> fileRecords,
            final String source) {
        LOGGER.info("Performing Delete Operation - Source: {} | Header: {}",
                source, fileHeader);

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
        LOGGER.info(
                "Performing Add Operation - Source: {} | Target: {} | Header: {}",
                source, target, fileHeader);

        /* Add Source */
        fileHeader.add(source);

        /* Add New Column Values */
        for (int index = 0; index < fileRecords.size(); index++) {
            FileRecord fileRecord = fileRecords.get(index);
            fileRecord.setIndex(index);
            Object expressionValue = evaluateExpression(fileRecord, target);
            fileRecord.getData().add(expressionValue);
        }
    }

    /**
     * Get Original Target
     * 
     * @param target
     * @return modifiedTarget
     */
    private String getOriginalTarget(final String target) {
        String modifiedTarget = target;
        for (final Entry<String, String> shortcutEntry : sMap.entrySet()) {
            modifiedTarget = modifiedTarget.replace(shortcutEntry.getKey(),
                    shortcutEntry.getValue());
        }
        return modifiedTarget;
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
        LOGGER.info(
                "Performing Move Operation - Source: {} | Target: {} | Header: {}",
                source, target, fileHeader);

        /* Get Source & Target Indices */
        int sourceIndex = fileHeader.indexOf(source);
        int targetIndex = Integer.parseInt(target.replace(
                Constants.COLUMN_ENCOLSING_QUOTE, Constants.EMPTY_STRING));

        /* Move Columns */
        String header = fileHeader.remove(sourceIndex);
        fileHeader.add(targetIndex, header);

        /* Move Data Column Values */
        for (final FileRecord fileRecord : fileRecords) {
            Object data = fileRecord.getData().remove(sourceIndex);
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
        LOGGER.info(
                "Performing Swap Operation - Source: {} | Target: {} | Header: {}",
                source, target, fileHeader);

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

    /**
     * Perform Include Operation
     * 
     * @param fileHeader
     * @param source
     * @param target
     * @param fileRecords
     */
    private void doInclude(List<String> fileHeader,
            List<FileRecord> fileRecords, final String target) {
        LOGGER.info("Performing Include Operation -  Target: {} | Header: {}",
                target, fileHeader);
        excludeRow(fileRecords, target, false);
    }

    /**
     * Perform Exclude Operation
     * 
     * @param fileHeader
     * @param source
     * @param target
     * @param fileRecords
     */
    private void doExclude(List<String> fileHeader,
            List<FileRecord> fileRecords, final String target) {
        LOGGER.info("Performing Exclude Operation -  Target: {} | Header: {}",
                target, fileHeader);
        excludeRow(fileRecords, target, true);
    }

    /**
     * Exclude Row
     * 
     * @param fileRecords
     * @param target
     * @param isExclude
     */
    private void excludeRow(List<FileRecord> fileRecords, final String target,
            final boolean isExclude) {
        /* Iterate Rows */
        Iterator<FileRecord> fileRecordsIterator = fileRecords.iterator();
        while (fileRecordsIterator.hasNext()) {
            FileRecord fileRecord = fileRecordsIterator.next();
            boolean expressionValue = (boolean) evaluateExpression(fileRecord,
                    target);
            if ((isExclude && expressionValue)
                    || !(isExclude || expressionValue)) {
                fileRecordsIterator.remove();
            }
        }
    }

    /**
     * Evaluate Expression
     * 
     * @param fileRecord
     * @param target
     * @return Evaluated Expression
     */
    private Object evaluateExpression(final FileRecord fileRecord,
            final String target) {
        Object expressionValue = expressionEvaluator.evaluate(fileRecord,
                CollectionUtils.isEmpty(sMap) ? target
                        : getOriginalTarget(target));
        LOGGER.info("Evaluated Expression Value: {}", expressionValue);
        return expressionValue;
    }
}
