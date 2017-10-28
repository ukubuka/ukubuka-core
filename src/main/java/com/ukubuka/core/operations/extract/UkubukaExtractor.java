package com.ukubuka.core.operations.extract;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.ukubuka.core.exception.ParserException;
import com.ukubuka.core.exception.PipelineException;
import com.ukubuka.core.model.FileContents;
import com.ukubuka.core.model.UkubukaSchema;
import com.ukubuka.core.model.UkubukaSchema.Extract;
import com.ukubuka.core.operations.UkubukaOperations;
import com.ukubuka.core.parser.UkubukaParser;

/**
 * Ukubuka Extractor
 * 
 * @author agrawroh
 * @version v1.0
 */
@Component("UkubukaExtractor")
public class UkubukaExtractor implements UkubukaOperations {

    /************************************ Logger Instance ***********************************/
    private static final Logger LOGGER = LoggerFactory
            .getLogger(UkubukaExtractor.class);

    /******************************** Dependency Injections *********************************/
    @Autowired
    @Qualifier("UkubukaXMLParser")
    private UkubukaParser xmlParser;

    @Autowired
    @Qualifier("UkubukaDFileParser")
    private UkubukaParser delimitedFileParser;

    /**
     * Perform Operations
     * 
     * @param dataFiles
     * @param schema
     * @throws PipelineException
     */
    public void performOperations(Map<String, FileContents> dataFiles,
            final UkubukaSchema schema) throws PipelineException {
        try {
            performOperations(dataFiles, schema.getExtracts());
        } catch (ParserException ex) {
            throw new PipelineException(ex);
        }
    }

    /**
     * Perform Extracts
     * 
     * @param dataFiles
     * @param extracts
     * @throws ParserException
     */
    private void performOperations(Map<String, FileContents> dataFiles,
            final List<Extract> extracts) throws ParserException {
        /* Iterate Extracts */
        for (final Extract extract : extracts) {
            LOGGER.info("Performing Extract: HC{}", extract.hashCode());
            FileContents fileContents;

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

            /* Store DataSet */
            dataFiles.put(extract.getId(), fileContents);
        }
    }
}
