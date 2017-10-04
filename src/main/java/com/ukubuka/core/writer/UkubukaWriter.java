package com.ukubuka.core.writer;

import java.io.IOException;
import java.util.List;

import org.json.CDL;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ukubuka.core.model.FileRecord;
import com.ukubuka.core.utilities.Constants;

/**
 * Ukubuka Writer
 * 
 * @author agrawroh
 * @version v1.0
 */
@Component
public class UkubukaWriter {

    /************************* Dependency Injections *************************/
    @Autowired
    private ObjectMapper mapper;

    /**
     * Write JSON
     * 
     * @param fileHeader
     * @param fileRecords
     * @return JSONArray
     */
    public JSONArray writeJSON(List<String> fileHeader,
            List<FileRecord> fileRecords) {
        /* CReate New Builder Instance */
        StringBuilder fileContents = new StringBuilder();

        /* Append Header */
        fileContents.append(
                StringUtils.arrayToCommaDelimitedString(fileHeader.toArray()))
                .append(Constants.DEFAULT_FILE_END_LINE_DELIMITER);

        /* Iterate Records */
        for (final FileRecord fileRecord : fileRecords) {
            /* Append Record */
            fileContents.append(
                    StringUtils.arrayToCommaDelimitedString(fileRecord
                            .getData().toArray())).append(
                    Constants.DEFAULT_FILE_END_LINE_DELIMITER);
        }

        /* Convert To JSON */
        return CDL.toJSONArray(fileContents.toString());
    }

    /**
     * Pretty Print JSON
     * 
     * @param jsonArray
     * @return Output JSON
     * @throws IOException
     */
    public String prettyPrint(final String jsonArray) throws IOException {
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(
                mapper.readValue(jsonArray, Object.class));
    }
}
