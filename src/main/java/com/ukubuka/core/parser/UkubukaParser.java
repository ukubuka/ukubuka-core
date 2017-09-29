package com.ukubuka.core.parser;

import com.ukubuka.core.exception.ParserException;

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
     * @return File Contents
     * @throws ParserException
     */
    String parseFile() throws ParserException;

    /**
     * Get Parser Information
     * 
     * @return Parser Info
     * @throws ParserException
     */
    String getParserInfo();
}
