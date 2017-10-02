package com.ukubuka.core.parser.impl;

import java.util.Map;

import org.json.JSONArray;
import org.springframework.stereotype.Component;

import com.ukubuka.core.exception.ParserException;
import com.ukubuka.core.parser.UkubukaParser;

@Component
public class UkubukaXMLParser implements UkubukaParser {

    @Override
    public JSONArray parseFile(String completeFileName,
            Map<String, String> flags) throws ParserException {
        // TODO Auto-generated method stub
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
