package com.ukubuka.core.operations.load;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ukubuka.core.exception.ParserException;
import com.ukubuka.core.exception.WriterException;
import com.ukubuka.core.model.FileContents;
import com.ukubuka.core.model.FileRecord;
import com.ukubuka.core.model.LoadOperation;
import com.ukubuka.core.model.SupportedFileType;
import com.ukubuka.core.model.UkubukaSchema.Load;
import com.ukubuka.core.writer.UkubukaWriter;

/**
 * Ukubuka Loader
 * 
 * @author agrawroh
 * @version v1.0
 */
@Component
public class UkubukaLoader {

    /************************************ Logger Instance ***********************************/
    private static final Logger LOGGER = LoggerFactory
            .getLogger(UkubukaLoader.class);

    /******************************** Dependency Injections *********************************/
    @Autowired
    private UkubukaWriter writer;

    /**
     * Perform Operations
     * 
     * @param fileHeader
     * @param operationsList
     * @param fileRecords
     * @throws WriterException 
     */
    public void performOperations(Map<String, FileContents> dataFiles,
            final List<Load> loads) throws WriterException {
        /* Iterate Operations */
        for (final Load load : loads) {
            performLoad(load, dataFiles);
        }
    }

    /**
     * Perform Loads
     * 
     * @param load
     * @param dataFiles
     * @throws WriterException
     */
    private void performLoad(final Load load,
            final Map<String, FileContents> dataFiles) throws WriterException {
        /* Check Whether Valid Load Operations */
        LOGGER.info("Performing Load: HC{}", load.hashCode());

        /* Get File Contents */
        FileContents fileContents = new FileContents(new ArrayList<String>(),
                new ArrayList<FileRecord>());
        fileContents.setHeader(
                dataFiles.get(load.getOperations().getHeader()).getHeader());

        /* Iterate Data Sources */
        for (final String fileId : load.getOperations().getData()) {
            /* Check Flag For DISTINCT */
            fileContents.getData().addAll(
                    LoadOperation.DISTINCT == load.getOperations().getFilter()
                            ? new HashSet<>(dataFiles.get(fileId).getData())
                            : dataFiles.get(fileId).getData());
        }

        /* Write File */
        LOGGER.info("Writing File...");
        try {
            LOGGER.info("ID: {} | Type: {} | Location: {}", load.getId(),
                    load.getType(), load.getLocation());
            writeFile(load.getType(), load.getLocation(),
                    fileContents.getHeader(), fileContents.getData());
        } catch (ParserException ex) {
            throw new WriterException(ex);
        }
    }

    /**
     * Write File
     * 
     * @param supportedFileType
     * @param completeFileName
     * @param header
     * @param data
     * @throws ParserException
     * @throws WriterException 
     */
    private void writeFile(final SupportedFileType supportedFileType,
            final String completeFileName, List<String> header,
            List<FileRecord> data) throws ParserException, WriterException {
        /* Get File Type */
        switch (supportedFileType) {
        /* Delimited File */
        case CSV:
            writer.writeFile(completeFileName, writer.writeCSV(header, data));
            break;
        /* XML File */
        case JSON:
            writer.writeFile(completeFileName, writer
                    .prettyPrint(writer.writeJSON(header, data).toString()));
            break;
        /* Unsupported File */
        default:
            throw new ParserException("File Type Not Supported!");
        }
    }
}
