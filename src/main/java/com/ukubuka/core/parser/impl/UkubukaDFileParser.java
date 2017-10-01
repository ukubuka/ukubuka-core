package com.ukubuka.core.parser.impl;

import org.json.CDL;
import org.json.JSONArray;
import org.json.JSONObject;
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
@Component
public class UkubukaDFileParser extends UkubukaBaseParser implements
        UkubukaParser {

    public static void main(String[] args) {
        try {
            new UkubukaDFileParser().parseFile(null);
        } catch (ParserException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * Parse File
     */
    public String parseFile(final String completeFileName)
            throws ParserException {
        System.out.println("A,B,C,\"D,E\"".replaceAll("/(,)(?=(?:[^\"]|\"[^\"]*\")*$)/", "|"));
        JSONArray foo = CDL
                .toJSONArray("Name|Age|City\nRohit|25|\"Boston,MA\"\nMohi|25|\"New Jersey,NY\"");
        for (int i = 0; i < foo.length(); i++) {
            JSONObject o = foo.getJSONObject(i);
            o.put("new_col", "Z");
        }
        System.out.println(foo);
        return null;
    }

    /**
     * Get Parser Information
     */
    public String getParserInfo() {
        return this.getClass().getSimpleName();
    }
}
