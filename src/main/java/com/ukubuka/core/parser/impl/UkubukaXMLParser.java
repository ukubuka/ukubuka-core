package com.ukubuka.core.parser.impl;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.ukubuka.core.exception.ParserException;
import com.ukubuka.core.model.FileContents;
import com.ukubuka.core.parser.UkubukaParser;

/**
 * Ukubuka XML Parser
 * 
 * @author agrawroh
 * @version v1.0
 */
@Component("UkubukaXMLParser")
public class UkubukaXMLParser implements UkubukaParser {

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
        return null;
    }

    /**
     * Get Parser Information
     */
    @Override
    public String getParserInfo() {
        return this.getClass().getSimpleName();
    }
}
