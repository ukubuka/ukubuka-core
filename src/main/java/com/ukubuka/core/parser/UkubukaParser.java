package com.ukubuka.core.parser;

import java.util.Map;

import org.json.JSONArray;

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
     * @param completeFileName
     * @param flags
     * @return JSON Array For File Contents
     * @throws ParserException
     */
    JSONArray parseFile(final String completeFileName, Map<String, Object> flags)
            throws ParserException;

    /**
     * Get Parser Information
     * 
     * @return Parser Info
     * @throws ParserException
     */
    String getParserInfo();
}
