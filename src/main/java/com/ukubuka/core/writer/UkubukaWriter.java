package com.ukubuka.core.writer;

import java.io.IOException;
import java.util.List;

import org.json.CDL;
import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ukubuka.core.exception.WriterException;
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

    /************************************ Logger Instance ***********************************/
    private static final Logger LOGGER = LoggerFactory
            .getLogger(UkubukaWriter.class);

    /******************************** Dependency Injections *********************************/
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
        LOGGER.info("Writing JSON: #" + fileRecords.size() + " Records");

        /* Convert To JSON */
        return CDL.toJSONArray(knitFile(fileHeader, fileRecords));
    }

    /**
     * Write CSV
     * 
     * @param fileHeader
     * @param fileRecords
     * @return CSV String
     */
    public String writeCSV(List<String> fileHeader,
            List<FileRecord> fileRecords) {
        LOGGER.info("Writing CSV: #" + fileRecords.size() + " Records");

        /* Convert To CSV */
        return knitFile(fileHeader, fileRecords);
    }

    /**
     * Knit File
     * 
     * @param fileHeader
     * @param fileRecords
     * @return Delimited String
     */
    public String knitFile(List<String> fileHeader,
            List<FileRecord> fileRecords) {
        LOGGER.info("Knitting File...");

        /* Create New Builder Instance */
        StringBuilder fileContents = new StringBuilder();

        /* Append Header */
        fileContents
                .append(StringUtils
                        .arrayToCommaDelimitedString(fileHeader.toArray()))
                .append(Constants.DEFAULT_FILE_END_LINE_DELIMITER);

        /* Iterate Records */
        for (final FileRecord fileRecord : fileRecords) {
            /* Append Record */
            fileContents
                    .append(StringUtils.arrayToCommaDelimitedString(
                            fileRecord.getData().toArray()))
                    .append(Constants.DEFAULT_FILE_END_LINE_DELIMITER);
        }

        /* Vomit String */
        String outputFileContent = fileContents.toString();
        LOGGER.info(
                "Output Content Bytes: " + outputFileContent.getBytes().length);
        return outputFileContent;
    }

    /**
     * Pretty Print JSON
     * 
     * @param jsonArray
     * @return Output JSON
     * @throws WriterException
     */
    public String prettyPrint(final String jsonArray) throws WriterException {
        LOGGER.info("Pretty Printing JSON...");
        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(
                    mapper.readValue(jsonArray, Object.class));
        } catch (IOException ex) {
            throw new WriterException(ex);
        }
    }
}
