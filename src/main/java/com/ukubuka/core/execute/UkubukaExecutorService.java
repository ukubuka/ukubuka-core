package com.ukubuka.core.execute;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.ukubuka.core.exception.ParserException;
import com.ukubuka.core.exception.ReaderException;
import com.ukubuka.core.model.SupportedSource;
import com.ukubuka.core.model.UkubukaSchema;
import com.ukubuka.core.model.UkubukaSchema.Extract;
import com.ukubuka.core.parser.UkubukaParser;
import com.ukubuka.core.reader.UkubukaReader;
import com.ukubuka.core.utilities.Constants;

/**
 * Ukubuka Executor Service
 * 
 * @author agrawroh
 * @version v1.0
 */
@Service
public class UkubukaExecutorService {
    /************************* Global Variables *************************/
    private static final ObjectReader SCHEMA_READER = new ObjectMapper()
            .readerFor(UkubukaSchema.class);

    /********************** Dependency Injections ***********************/
    @Autowired
    private UkubukaReader reader;

    @Autowired
    @Qualifier("UkubukaDFileParser")
    private UkubukaParser delimitedFileParser;

    @Autowired
    @Qualifier("UkubukaXMLParser")
    private UkubukaParser xmlParser;

    /**
     * Execute Ukubuka
     * 
     * @param ukubukaSchemaFile
     * @throws ParserException
     */
    public void execute(final String ukubukaSchemaFile) throws ParserException {
        /* Read File*/
        UkubukaSchema ukubukaSchema = readSchema(ukubukaSchemaFile);
        for (final Extract extract : ukubukaSchema.getExtracts()) {
            /* Get File Type */
            switch (extract.getType()) {
            /* Delimited File */
                case CSV:
                    System.out.println(delimitedFileParser.parseFile(
                            extract.getLocation(), extract.getFlags()));
                    break;
                /* XML File */
                case XML:
                    System.out.println(xmlParser.parseFile(
                            extract.getLocation(), extract.getFlags()));
                    break;
                /* Unsupported File */
                default:
                    throw new ParserException("File Type Not Supported!");
            }
        }
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
