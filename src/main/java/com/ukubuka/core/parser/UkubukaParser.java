package com.ukubuka.core.parser;

import java.util.Map;

import com.ukubuka.core.exception.ParserException;
import com.ukubuka.core.model.FileContents;

/**
 * Ukubuka Parser Interface
 * 
 * @author agrawroh
 * @version v1.0
 */
public interface UkubukaParser {

    /**
     * File Parser
     * 
     * @param completeFileName
     * @param flags
     * @return File Contents
     * @throws ParserException
     */
    FileContents parseFile(final String completeFileName,
            Map<String, Object> flags) throws ParserException;

    /**
     * Get Parser Information
     * 
     * @return Parser Info
     * @throws ParserException
     */
    String getParserInfo();
}
