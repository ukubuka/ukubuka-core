package com.ukubuka.core.schema;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.ukubuka.core.exception.ParserException;
import com.ukubuka.core.exception.ReaderException;
import com.ukubuka.core.model.SupportedSource;
import com.ukubuka.core.model.UkubukaSchema;
import com.ukubuka.core.reader.UkubukaReader;
import com.ukubuka.core.utilities.Constants;

/**
 * Ukubuka Schema Reader
 * @author agrawroh
 * @version v1.0
 */
@Component
public class UkubukaSchemaReader {

    /************************************ Logger Instance ***********************************/
    private static final Logger LOGGER = LoggerFactory
            .getLogger(UkubukaSchemaReader.class);

    /*********************************** Global Variables ***********************************/
    private static final ObjectReader SCHEMA_READER = new ObjectMapper()
            .readerFor(UkubukaSchema.class);

    /********************************* Dependency Injections ********************************/
    @Autowired
    private UkubukaReader reader;

    /**
     * Read Ukubuka Schema
     * 
     * @param ukubukaSchemaFile
     * @return Ukubuka Schema
     * @throws ParserException
     */
    public UkubukaSchema readSchema(final String ukubukaSchemaFile)
            throws ParserException {
        LOGGER.info("Start Reading Ukubuka Schema...");
        try {
            return SCHEMA_READER.readValue(reader.readFileAsString(
                    SupportedSource.FILE, ukubukaSchemaFile,
                    Constants.DEFAULT_FILE_ENCODING));
        } catch (ReaderException | IOException ex) {
            throw new ParserException(ex);
        }
    }
}
