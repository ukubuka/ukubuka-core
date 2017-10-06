package com.ukubuka.core.execute;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.ukubuka.core.exception.ParserException;
import com.ukubuka.core.exception.ReaderException;
import com.ukubuka.core.exception.TransformException;
import com.ukubuka.core.exception.WriterException;
import com.ukubuka.core.model.FileContents;
import com.ukubuka.core.model.FileRecord;
import com.ukubuka.core.model.LoadOperation;
import com.ukubuka.core.model.SupportedFileType;
import com.ukubuka.core.model.SupportedSource;
import com.ukubuka.core.model.UkubukaSchema;
import com.ukubuka.core.model.UkubukaSchema.Extract;
import com.ukubuka.core.model.UkubukaSchema.Load;
import com.ukubuka.core.model.UkubukaSchema.Transform;
import com.ukubuka.core.model.UkubukaSchema.TransformOperations;
import com.ukubuka.core.parser.UkubukaParser;
import com.ukubuka.core.reader.UkubukaReader;
import com.ukubuka.core.transform.UkubukaTransformer;
import com.ukubuka.core.utilities.Constants;
import com.ukubuka.core.utilities.Utilities;
import com.ukubuka.core.writer.UkubukaWriter;

/**
 * Ukubuka Executor Service
 * 
 * @author agrawroh
 * @version v1.0
 */
@Service
public class UkubukaExecutorService {

    /************************************ Logger Instance ***********************************/
    private static final Logger LOGGER = LoggerFactory
            .getLogger(UkubukaExecutorService.class);

    /*********************************** Global Variables ***********************************/
    private static final ObjectReader SCHEMA_READER = new ObjectMapper()
            .readerFor(UkubukaSchema.class);

    /********************************* Dependency Injections ********************************/
    @Autowired
    private UkubukaReader reader;

    @Autowired
    private UkubukaWriter writer;

    @Autowired
    @Qualifier("UkubukaXMLParser")
    private UkubukaParser xmlParser;

    @Autowired
    @Qualifier("UkubukaDFileParser")
    private UkubukaParser delimitedFileParser;

    @Autowired
    private UkubukaTransformer ukubukaTransformer;

    /**
     * Execute Ukubuka
     * 
     * @param ukubukaSchemaFile
     * @throws ParserException
     * @throws TransformException
     * @throws WriterException
     */
    public void execute(final String ukubukaSchemaFile) throws ParserException,
            TransformException, WriterException {
        /* Create An In-Memory Data Store */
        Map<String, FileContents> dataFiles = new HashMap<>();

        /* Read File*/
        UkubukaSchema ukubukaSchema = readSchema(ukubukaSchemaFile);

        /* Iterate Extracts */
        LOGGER.info("Performing Extracts...");
        for (final Extract extract : ukubukaSchema.getExtracts()) {
            LOGGER.info("Performing Extract: HC" + extract.hashCode());
            FileContents fileContents = null;

            /* Get File Type */
            switch (extract.getType()) {
            /* Delimited File */
                case CSV:
                    fileContents = delimitedFileParser.parseFile(
                            extract.getLocation(), extract.getFlags());
                    break;
                /* XML File */
                case XML:
                    fileContents = xmlParser.parseFile(extract.getLocation(),
                            extract.getFlags());
                    break;
                /* Unsupported File */
                default:
                    throw new ParserException("File Type Not Supported!");
            }

            /* Perform Transformations */
            LOGGER.info("Performing Transformations...");
            performTransformation(extract.getId(),
                    ukubukaSchema.getTransforms(), fileContents);

            /* Store DataSet */
            dataFiles.put(extract.getId(), fileContents);
        }

        /* Perform Load */
        LOGGER.info("Performing Load...");
        performLoad(ukubukaSchema.getLoads(), dataFiles);
    }

    /**
     * Perform Transformations
     * 
     * @param fileId
     * @param transforms
     * @param fileContents
     * @throws TransformException
     */
    private void performTransformation(final String fileId,
            final List<Transform> transforms, FileContents fileContents)
            throws TransformException {
        /* Get File Transformation */
        List<TransformOperations> fileTransforms = getFileTransformationDetails(
                fileId, transforms);
        if (!CollectionUtils.isEmpty(fileTransforms)) {
            LOGGER.info("Transform Count: #" + fileTransforms.size());
            ukubukaTransformer.performOperations(fileContents.getHeader(),
                    fileContents.getData(), fileTransforms);
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
        if (null != load) {
            LOGGER.info("Performing Load: HC" + load.hashCode());

            /* Get File Contents */
            FileContents fileContents = new FileContents(
                    new ArrayList<String>(), new ArrayList<FileRecord>());
            fileContents.setHeader(dataFiles.get(
                    load.getOperations().getHeader()).getHeader());

            /* Iterate Data Sources */
            for (final String fileId : load.getOperations().getData()) {
                /* Check Flag For DISTINCT */
                fileContents.getData().addAll(
                        LoadOperation.DISTINCT == load.getOperations()
                                .getFilter() ? new HashSet<>(dataFiles.get(
                                fileId).getData()) : dataFiles.get(fileId)
                                .getData());
            }

            /* Write File */
            LOGGER.info("Writing File...");
            try {
                LOGGER.info("ID: " + load.getId() + " | Type: "
                        + load.getType() + " | Location: " + load.getLocation());
                writeFile(load.getType(), load.getLocation(),
                        fileContents.getHeader(), fileContents.getData());
            } catch (ParserException | IOException ex) {
                throw new WriterException(ex);
            }
        }
    }

    /**
     * Write File
     * 
     * @param supportedFileType
     * @param completeFileName
     * @param header
     * @param data
     * @throws IOException
     * @throws ParserException
     */
    private void writeFile(final SupportedFileType supportedFileType,
            final String completeFileName, List<String> header,
            List<FileRecord> data) throws IOException, ParserException {
        /* Get File Type */
        switch (supportedFileType) {
        /* Delimited File */
            case CSV:
                Utilities.writeFile(completeFileName,
                        writer.writeCSV(header, data).toString());
                break;
            /* XML File */
            case JSON:
                Utilities.writeFile(completeFileName, writer.prettyPrint(writer
                        .writeJSON(header, data).toString()));
                break;
            /* Unsupported File */
            default:
                throw new ParserException("File Type Not Supported!");
        }
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
     * Read Ukubuka Schema
     * 
     * @param ukubukaSchemaFile
     * @return Ukubuka Schema
     * @throws ParserException
     */
    private UkubukaSchema readSchema(final String ukubukaSchemaFile)
            throws ParserException {
        try {
            return SCHEMA_READER.readValue(reader.readFileAsString(
                    SupportedSource.FILE, ukubukaSchemaFile,
                    Constants.DEFAULT_FILE_ENCODING));
        } catch (ReaderException | IOException ex) {
            throw new ParserException(ex);
        }
    }
}
