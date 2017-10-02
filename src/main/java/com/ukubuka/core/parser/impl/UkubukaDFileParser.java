package com.ukubuka.core.parser.impl;

import java.util.Map;

import org.json.CDL;
import org.json.JSONArray;
import org.springframework.stereotype.Component;

import com.ukubuka.core.exception.ParserException;
import com.ukubuka.core.parser.UkubukaBaseParser;
import com.ukubuka.core.parser.UkubukaParser;

/**
 * Ukubuka Delimited File Parser
 * 
 * @author agrawroh
 * @version v1.0
 */
@Component("UkubukaDFileParser")
public class UkubukaDFileParser extends UkubukaBaseParser implements
        UkubukaParser {

    /**
     * Parse File
     */
    @Override
    public JSONArray parseFile(final String completeFileName,
            Map<String, Object> flags) throws ParserException {
        return CDL.toJSONArray(super.readWithOptions(completeFileName, flags));
    }

    /**
     * Get Parser Information
     */
    @Override
    public String getParserInfo() {
        return this.getClass().getSimpleName();
    }
}
