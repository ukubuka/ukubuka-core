package com.ukubuka.core.execute;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.ukubuka.core.exception.ParserException;
import com.ukubuka.core.exception.ReaderException;
import com.ukubuka.core.exception.TransformException;
import com.ukubuka.core.model.FileContents;
import com.ukubuka.core.model.SupportedSource;
import com.ukubuka.core.model.UkubukaSchema;
import com.ukubuka.core.model.UkubukaSchema.Extract;
import com.ukubuka.core.model.UkubukaSchema.Operations;
import com.ukubuka.core.model.UkubukaSchema.Transform;
import com.ukubuka.core.parser.UkubukaParser;
import com.ukubuka.core.reader.UkubukaReader;
import com.ukubuka.core.transform.UkubukaTransformer;
import com.ukubuka.core.utilities.Constants;
import com.ukubuka.core.writer.UkubukaWriter;

/**
 * Ukubuka Executor Service
 * 
 * @author agrawroh
 * @version v1.0
 */
@Service
public class UkubukaExecutorService {
    /**************************** Global Variables ***************************/
    private static final ObjectReader SCHEMA_READER = new ObjectMapper()
            .readerFor(UkubukaSchema.class);

    /************************* Dependency Injections *************************/
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
     */
    public void execute(final String ukubukaSchemaFile) throws ParserException,
            TransformException {
        /* Read File*/
        UkubukaSchema ukubukaSchema = readSchema(ukubukaSchemaFile);
        for (final Extract extract : ukubukaSchema.getExtracts()) {
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

            /* Get File Transformation */
            List<Operations> fileTransforms = getFileTransformationDetails(
                    extract.getId(), ukubukaSchema.getTransforms());
            if (!CollectionUtils.isEmpty(fileTransforms)) {
                ukubukaTransformer.performOperations(fileContents.getHeader(),
                        fileContents.getData(), fileTransforms);
                System.out.println(writer.writeJSON(fileContents.getHeader(),
                        fileContents.getData()));
            }
        }
    }

    /**
     * Get File Transformation Details
     * 
     * @param fileId
     * @param transforms
     * @return File Transforms
     */
    private List<Operations> getFileTransformationDetails(final String fileId,
            List<Transform> transforms) {
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
