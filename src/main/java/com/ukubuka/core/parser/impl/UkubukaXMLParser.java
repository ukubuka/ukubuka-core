package com.ukubuka.core.parser.impl;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.ukubuka.core.exception.ParserException;
import com.ukubuka.core.exception.ReaderException;
import com.ukubuka.core.model.ExtractFlags;
import com.ukubuka.core.model.FileContents;
import com.ukubuka.core.model.SupportedSource;
import com.ukubuka.core.parser.UkubukaBaseParser;
import com.ukubuka.core.parser.UkubukaParser;
import com.ukubuka.core.utilities.Constants;
import com.ukubuka.core.utilities.Utilities;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;

/**
 * Ukubuka XML Parser
 * 
 * @author aswinv
 * @version v1.0
 */
@Component("UkubukaXMLParser")
public class UkubukaXMLParser extends UkubukaBaseParser implements UkubukaParser {

    /************************************ Logger Instance ***********************************/
    private static final Logger LOGGER = LoggerFactory
            .getLogger(UkubukaDFileParser.class);

    /**
     * Parse File
     */
    @Override
    public FileContents parseFile(final String completeFileName,
            Map<String, Object> flags) throws ParserException {
        LOGGER.info("Parsing XML File - Location: {} | Flags: {}",
                completeFileName, flags);
        return super.getFileContents(readWithOptions(completeFileName, flags));
    }

    /**
     * Get Parser Information
     */
    @Override
    public String getParserInfo() {
        return this.getClass().getSimpleName();
    }
    
    /**
     * Read With Options
     * 
     * @param completeFileName
     * @return File Content
     * @throws ParserException
     */
    private List<String> readWithOptions(final String completeFileName,
                                         Map<String, Object> flags) throws ParserException {
        boolean withHeader = null == flags
                .get(ExtractFlags.FILE_CONTAINS_HEADER.getFlag()) || (boolean) flags.get(
                ExtractFlags.FILE_CONTAINS_HEADER.getFlag());
        SupportedSource source = null == flags
                .get(ExtractFlags.SOURCE.getFlag()) ? SupportedSource.FILE
                        : SupportedSource.getSource((String) flags
                                .get(ExtractFlags.SOURCE.getFlag()));
        List<String> fileContents = readWithOptions(source, completeFileName);
        return withHeader ? fileContents : super.appendHeader(fileContents);
    }
    
    /**
     * Read With Options
     * 
     * @param completeFileName
     =* @return File Content
     * @throws ParserException
     */
    private List<String> readWithOptions(final SupportedSource source,
            final String completeFileName) throws ParserException {
        try {
            String[] fileContents = super.getReader()
                    .readXMLAsString(source, completeFileName)
                    .split(Constants.DEFAULT_FILE_END_LINE_DELIMITER);
            return new ArrayList<>(
                    Arrays.asList(fileContents));
        } catch (ReaderException ex) {
            throw new ParserException(ex);
        }
    }
}
