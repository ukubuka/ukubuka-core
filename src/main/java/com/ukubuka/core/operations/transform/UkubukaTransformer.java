package com.ukubuka.core.operations.transform;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
     * @param fileId
     * @param transforms
     * @param fileContents
     * @return 
     * @throws TransformException
     */
    public Map<String, FileContents> performOperations(
            Map<String, FileContents> dataFiles,
            final List<Transform> transforms) throws TransformException {
        /* Get File Transformation */
        for (final String key : dataFiles.keySet()) {
            List<TransformOperations> fileTransforms = getFileTransformationDetails(
                    key, transforms);
            if (!CollectionUtils.isEmpty(fileTransforms)) {
                LOGGER.info("Transform Count: #" + fileTransforms.size());
                performTransformOperations(dataFiles.get(key).getHeader(),
                        dataFiles.get(key).getData(), fileTransforms);
            }
        }
        return dataFiles;
    }

    /**
     * Get File Transformation Details
     * 
     * @param fileId
     * @param transforms
     * @return File Transforms
     */
    private List<TransformOperations> getFileTransformationDetails(
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
            LOGGER.info("Performing Transform: HC" + operation.hashCode());

            /* Check Whether Column Exists */
            String source = operation.getSource();
            if (operation.getType() != TransformOperation.ADD
                    && operation.getType() != TransformOperation.NEW
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
    private void doDelete(List<String> fileHeader, List<FileRecord> fileRecords,
            final String source) {
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
        for (int index = 0; index < fileRecords.size(); index++) {
            FileRecord fileRecord = fileRecords.get(index);
            fileRecord.setIndex(index);
            String expressionValue = String.valueOf(expressionEvaluator
                    .evaluate(fileRecord, CollectionUtils.isEmpty(sMap) ? target
                            : getOriginalTarget(target)));
            LOGGER.info("Evaluated Expression Value: " + expressionValue);
            fileRecord.getData().add(expressionValue);
        }
    }

    /**
     * Get Original Target
     * @param target
     * @return modifiedTarget
     */
    private String getOriginalTarget(final String target) {
        String modifiedTarget = target;
        for (final String key : sMap.keySet()) {
            modifiedTarget = modifiedTarget.replace(key, sMap.get(key));
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
