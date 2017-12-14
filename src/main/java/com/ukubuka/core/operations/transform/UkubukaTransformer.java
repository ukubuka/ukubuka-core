package com.ukubuka.core.operations.transform;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.ukubuka.core.evaluator.UkubukaExpressionEvaluator;
import com.ukubuka.core.exception.PipelineException;
import com.ukubuka.core.exception.TransformException;
import com.ukubuka.core.launcher.UkubukaLauncher;
import com.ukubuka.core.model.FileContents;
import com.ukubuka.core.model.FileRecord;
import com.ukubuka.core.model.SupportedSource;
import com.ukubuka.core.model.TransformOperation;
import com.ukubuka.core.model.UkubukaSchema;
import com.ukubuka.core.model.UkubukaSchema.Transform;
import com.ukubuka.core.model.UkubukaSchema.TransformOperations;
import com.ukubuka.core.model.UkubukaSchema.TransformOperationsType;
import com.ukubuka.core.operations.UkubukaOperations;
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
@Component("UkubukaTransformer")
public class UkubukaTransformer implements UkubukaOperations {

    /************************************ Logger Instance ***********************************/
    private static final Logger LOGGER = LoggerFactory
            .getLogger(UkubukaTransformer.class);

    /************************************ Global Variables **********************************/
    private static final EnumSet<TransformOperation> TRANSFORM_OPERATION_EXCEPTIONS = EnumSet
            .of(TransformOperation.ADD, TransformOperation.NEW,
                    TransformOperation.INCLUDE, TransformOperation.EXCLUDE,
                    TransformOperation.SUM, TransformOperation.AVG,
                    TransformOperation.MIN, TransformOperation.MAX,
                    TransformOperation.COUNT);

    /******************************** Dependency Injections *********************************/
    @Autowired
    private UkubukaExpressionEvaluator expressionEvaluator;

    @Autowired
    private UkubukaReader reader;

    /**
     * Perform Operations
     * 
     * @param dataFiles
     * @param schema
     * @throws PipelineException
     */
    @Override
    public void performOperations(Map<String, FileContents> dataFiles,
            final UkubukaSchema schema) throws PipelineException {
        try {
            performOperations(dataFiles, schema.getTransforms());
        } catch (TransformException ex) {
            throw new PipelineException(ex);
        }
    }

    /**
     * Perform Transformations
     * 
     * @param dataFiles
     * @param transforms
     * @throws TransformException
     */
    private void performOperations(Map<String, FileContents> dataFiles,
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
            performTransformOperations(fileContents, transformOperations);
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
    private void performTransformOperations(FileContents fileContents,
            List<TransformOperations> operationsList)
            throws TransformException {
        /* Iterate Operations */
        for (final TransformOperations operation : operationsList) {
            LOGGER.info("Performing Transform: HC{}", operation.hashCode());

            /* Check Whether Column Exists */
            String source = operation.getSource();
            if (!TRANSFORM_OPERATION_EXCEPTIONS.contains(operation.getType())
                    && !fileContents.getHeader().contains(source)) {
                throw new TransformException("Column Not Found! Name: " + source
                        + " | Header: " + fileContents.getHeader());
            }

            /* Perform Operation */
            performTransformOperation(fileContents, operation.getType(), source,
                    operation.getTarget());
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
    private void performTransformOperation(FileContents fileContents,
            final TransformOperation operationType, final String source,
            final String target) throws TransformException {
        /* Get File Contents */
        List<String> fileHeader = fileContents.getHeader();
        List<FileRecord> fileRecords = fileContents.getData();

        /* Switch Type */
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
                doAdd(fileContents, source, target);
                break;

            /* Column Add Operation */
            case NEW:
                doAdd(fileContents, source, target);
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
                doInclude(fileContents, target);
                break;

            /* Column Exclude Operation */
            case EXCLUDE:
                doExclude(fileContents, target);
                break;

            /* Column Sum Operation */
            case SUM:
                doSum(fileContents, source, target);
                break;

            /* Column Average Operation */
            case AVG:
                doAverage(fileContents, source, target);
                break;

            /* Column Minimum Operation */
            case MIN:
                doMin(fileContents, source, target);
                break;

            /* Column Maximum Operation */
            case MAX:
                doMax(fileContents, source, target);
                break;

            /* Column Count Operation */
            case COUNT:
                doCount(fileContents, source, target);
                break;

            /* Unsupported Operation */
            default:
                throw new TransformException(
                        "Unsupported Operation: " + operationType);
        }
    }

    /**
     * Perform Sum Operation
     * 
     * @param fileContents
     * @param source
     * @param target
     */
    private void doSum(FileContents fileContents, final String source,
            final String target) {
        LOGGER.info("Performing Sum Operation - Source: {} | Target: {}",
                source, target);

        /* Add Source */
        fileContents.getAggregations().put(source,
                calculateSum(fileContents, target));
    }

    /**
     * Perform Count Operation
     * 
     * @param fileContents
     * @param source
     * @param target
     */
    private void doCount(FileContents fileContents, final String source,
            final String target) {
        LOGGER.info("Performing Count Operation - Source: {} | Target: {}",
                source, target);

        /* Add Source */
        fileContents.getAggregations().put(source,
                new BigDecimal(fileContents.getData().size()));
    }

    /**
     * Perform Average Operation
     * 
     * @param fileContents
     * @param source
     * @param target
     */
    private void doAverage(FileContents fileContents, final String source,
            final String target) {
        LOGGER.info("Performing Average Operation - Source: {} | Target: {}",
                source, target);

        /* Add Source */
        fileContents.getAggregations().put(source,
                calculateSum(fileContents, target).divide(
                        new BigDecimal(fileContents.getData().size()),
                        Constants.DIVISION_OPERATION_PRECISION,
                        Constants.DIVISION_OPERATION_ROUNDING));
    }

    /**
     * Perform Minimum Operation
     * 
     * @param fileContents
     * @param source
     * @param target
     */
    private void doMin(FileContents fileContents, final String source,
            final String target) {
        LOGGER.info("Performing Minimum Operation - Source: {} | Target: {}",
                source, target);

        /* Add Source */
        fileContents.getAggregations().put(source,
                calculateMin(fileContents, target));
    }

    /**
     * Perform Maximum Operation
     * 
     * @param fileContents
     * @param source
     * @param target
     */
    private void doMax(FileContents fileContents, final String source,
            final String target) {
        LOGGER.info("Performing Maximum Operation - Source: {} | Target: {}",
                source, target);

        /* Add Source */
        fileContents.getAggregations().put(source,
                calculateMax(fileContents, target));
    }

    /**
     * Calculate Minimum
     * 
     * @param fileContents
     * @param target
     * @return sumValue
     */
    private BigDecimal calculateMin(FileContents fileContents,
            final String target) {
        /* Calculate Minimum Column Value */
        BigDecimal minValue = BigDecimal.valueOf(Double.MAX_VALUE);
        for (final FileRecord fileRecord : fileContents.getData()) {
            BigDecimal expressionValue = new BigDecimal(String.valueOf(
                    evaluateExpression(fileContents, fileRecord, target)));
            minValue = minValue.min(expressionValue);
        }
        return minValue;
    }

    /**
     * Calculate Maximum
     * 
     * @param fileContents
     * @param target
     * @return sumValue
     */
    private BigDecimal calculateMax(FileContents fileContents,
            final String target) {
        /* Calculate Maximum Column Value */
        BigDecimal maxValue = BigDecimal.valueOf(Double.MIN_VALUE);
        for (final FileRecord fileRecord : fileContents.getData()) {
            BigDecimal expressionValue = new BigDecimal(String.valueOf(
                    evaluateExpression(fileContents, fileRecord, target)));
            maxValue = maxValue.max(expressionValue);
        }
        return maxValue;
    }

    /**
     * Calculate Sum
     * 
     * @param fileContents
     * @param target
     * @return sumValue
     */
    private BigDecimal calculateSum(FileContents fileContents,
            final String target) {
        /* Sum Column Values */
        BigDecimal sumValue = new BigDecimal(0);
        for (final FileRecord fileRecord : fileContents.getData()) {
            BigDecimal expressionValue = new BigDecimal(String.valueOf(
                    evaluateExpression(fileContents, fileRecord, target)));
            sumValue = sumValue.add(expressionValue);
        }
        return sumValue;
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
     * @param fileContents
     * @param source
     * @param target
     */
    private void doAdd(FileContents fileContents, final String source,
            final String target) {
        List<String> fileHeader = fileContents.getHeader();
        List<FileRecord> fileRecords = fileContents.getData();
        LOGGER.info(
                "Performing Add Operation - Source: {} | Target: {} | Header: {}",
                source, target, fileHeader);

        /* Add Source */
        fileHeader.add(source);

        /* Add New Column Values */
        for (int index = 0; index < fileRecords.size(); index++) {
            FileRecord fileRecord = fileRecords.get(index);
            fileRecord.setIndex(index);
            Object expressionValue = evaluateExpression(fileContents,
                    fileRecord, target);
            fileRecord.getData().add(expressionValue);
        }
    }

    /**
     * Get Original Target
     * 
     * @param target
     * @param sMap
     * @return modifiedTarget
     */
    private String getOriginalTarget(final String target,
            final Map<String, String> sMap) {
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
     * @param fileContetns
     * @param target
     */
    private void doInclude(FileContents fileContetns, final String target) {
        LOGGER.info("Performing Include Operation -  Target: {} | Header: {}",
                target, fileContetns.getHeader());
        excludeRow(fileContetns, target, false);
    }

    /**
     * Perform Exclude Operation
     * 
     * @param fileContetns
     * @param target
     */
    private void doExclude(FileContents fileContetns, final String target) {
        LOGGER.info("Performing Exclude Operation -  Target: {} | Header: {}",
                target, fileContetns.getHeader());
        excludeRow(fileContetns, target, true);
    }

    /**
     * Exclude Row
     * 
     * @param fileContents
     * @param target
     * @param isExclude
     */
    private void excludeRow(FileContents fileContents, final String target,
            final boolean isExclude) {
        /* Iterate Rows */
        Iterator<FileRecord> fileRecordsIterator = fileContents.getData()
                .iterator();
        while (fileRecordsIterator.hasNext()) {
            FileRecord fileRecord = fileRecordsIterator.next();
            boolean expressionValue = (boolean) evaluateExpression(fileContents,
                    fileRecord, target);
            if ((isExclude && expressionValue)
                    || !(isExclude || expressionValue)) {
                fileRecordsIterator.remove();
            }
        }
    }

    /**
     * Evaluate Expression
     * 
     * @param fileContents
     * @param target
     * @return Evaluated Expression
     */
    private Object evaluateExpression(final FileContents fileContents,
            final FileRecord fileRecord, final String target) {
        Map<String, String> sMap = getShortcutsMap();
        Object expressionValue = expressionEvaluator.evaluate(fileContents,
                fileRecord, CollectionUtils.isEmpty(sMap) ? target
                        : getOriginalTarget(target, sMap));
        LOGGER.info("Evaluated Expression Value: {}", expressionValue);
        return expressionValue;
    }

    /**
     * Get Shortcuts Map
     * 
     * @return sMap
     */
    private Map<String, String> getShortcutsMap() {
        /* Get Mappings File */
        String[] fileContents;
        try {
            fileContents = this.reader
                    .readFileAsString(SupportedSource.FILE, UkubukaLauncher
                            .getAppContext()
                            .getResource(
                                    Constants.CLASSPATH + "shortcut-mappings")
                            .getFile().getAbsolutePath(),
                            Constants.DEFAULT_FILE_ENCODING)
                    .split(Constants.DEFAULT_FILE_END_LINE_DELIMITER);
        } catch (Exception ex) {
            LOGGER.error("Unable To Read Shortcut Mappings!", ex);
            return Collections.emptyMap();
        }

        /* Create Shortcuts Map */
        Map<String, String> sMap = new HashMap<>();
        for (final String fileContent : fileContents) {
            String[] keyValuePair = fileContent
                    .split(Constants.SHORTCUT_MAP_DELIMITER);
            sMap.put(keyValuePair[0], keyValuePair[1]);
        }
        return sMap;
    }
}
