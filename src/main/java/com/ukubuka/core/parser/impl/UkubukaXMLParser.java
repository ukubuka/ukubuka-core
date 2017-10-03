package com.ukubuka.core.parser.impl;

import java.util.Map;

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

    @Override
    public FileContents parseFile(String completeFileName,
            Map<String, Object> flags) throws ParserException {
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
