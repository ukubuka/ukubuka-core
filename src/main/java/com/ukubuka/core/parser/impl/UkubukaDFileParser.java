package com.ukubuka.core.parser.impl;

import java.util.Map;

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

/**
 * Ukubuka Delimited File Parser
 * 
 * @author agrawroh
 * @version v1.0
 */
@Component("UkubukaDFileParser")
public class UkubukaDFileParser extends UkubukaBaseParser implements
        UkubukaParser {

    /************************************ Logger Instance ***********************************/
    private static final Logger LOGGER = LoggerFactory
            .getLogger(UkubukaDFileParser.class);

    /**
     * Parse File
     */
    @Override
    public FileContents parseFile(final String completeFileName,
            Map<String, Object> flags) throws ParserException {
        LOGGER.info("Parsing Delimited File - Location: " + completeFileName
                + " | Flags: " + flags);
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
    public String readWithOptions(final String completeFileName,
            Map<String, Object> flags) throws ParserException {
        boolean withHeader = null != flags
                .get(ExtractFlags.FILE_CONTAINS_HEADER.getFlag()) ? (boolean) flags
                .get(ExtractFlags.FILE_CONTAINS_HEADER.getFlag()) : true;
        SupportedSource source = null == flags.get(ExtractFlags.SOURCE
                .getFlag()) ? SupportedSource.FILE : SupportedSource
                .getSource((String) flags.get(ExtractFlags.SOURCE.getFlag()));
        String fileContents = readWithOptions(source, completeFileName,
                (String) flags.get(ExtractFlags.FILE_ENCODING.getFlag()),
                (String) flags.get(ExtractFlags.FILE_DELIMITER.getFlag()));
        return withHeader ? fileContents : super.appendHeader(fileContents);
    }

    /**
     * Read With Options
     * 
     * @param completeFileName
     * @param fileEncoding
     * @param fileDelimiter
     * @return File Content
     * @throws ParserException
     */
    private String readWithOptions(final SupportedSource source,
            final String completeFileName, final String fileEncoding,
            final String fileDelimiter) throws ParserException {
        try {
            return StringUtils.isEmpty(fileDelimiter) ? super.getReader()
                    .readFileAsString(source, completeFileName, fileEncoding)
                    : super.getReader()
                            .readFileAsString(source, completeFileName,
                                    fileEncoding)
                            .replaceAll(
                                    Constants.DELIMITER_REPLACE_REGEX_START
                                            + fileDelimiter
                                            + Constants.DELIMITER_REPLACE_REGEX_END,
                                    Constants.DEFAULT_FILE_DELIMITER);
        } catch (ReaderException ex) {
            throw new ParserException(ex);
        }
    }
}
