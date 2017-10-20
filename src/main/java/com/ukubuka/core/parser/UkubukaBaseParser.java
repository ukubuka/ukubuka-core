package com.ukubuka.core.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import com.ukubuka.core.model.FileContents;
import com.ukubuka.core.model.FileRecord;
import com.ukubuka.core.reader.UkubukaReader;
import com.ukubuka.core.utilities.Constants;

/**
 * Ukubuka Base Parser
 * 
 * @author agrawroh
 * @version v1.0
 */
public class UkubukaBaseParser {

    /************************************ Logger Instance ***********************************/
    private static final Logger LOGGER = LoggerFactory
            .getLogger(UkubukaBaseParser.class);

    /******************************** Dependency Injections *********************************/
    @Autowired
    private UkubukaReader reader;

    /**
     * Get File Contents
     * 
     * @param fileContent
     * @return File Contents
     */
    public FileContents getFileContents(final String fileContent) {
        FileContents fileContents = new FileContents();

        /* Get File Lines */
        String[] fileLines = fileContent
                .split(Constants.DEFAULT_FILE_END_LINE_DELIMITER);

        /* Set Header */
        fileContents.setHeader(new ArrayList<>(Arrays
                .asList(fileLines[0].split(Constants.DEFAULT_FILE_DELIMITER))));

        /* Set Data */
        List<FileRecord> fileData = new ArrayList<>();
        for (int i = 1; i < fileLines.length; i++) {
            fileData.add(new FileRecord(new ArrayList<>(Arrays.asList(
                    fileLines[i].split(Constants.DEFAULT_FILE_DELIMITER)))));
        }
        fileContents.setData(fileData);

        return fileContents;
    }

    /**
     * Get File Contents
     * 
     * @param fileContent
     * @return File Contents
     */
    public FileContents getFileContents(final List<String> fileContent) {
        FileContents fileContents = new FileContents();

        /* Set Header */
        fileContents.setHeader(new ArrayList<>(Arrays.asList(fileContent
                .remove(0).split(Constants.DEFAULT_FILE_DELIMITER))));

        /* Set Data */
        List<FileRecord> fileData = new ArrayList<>();
        while (!CollectionUtils.isEmpty(fileContent)) {
            fileData.add(new FileRecord(
                    new ArrayList<>(Arrays.asList(fileContent.remove(0)
                            .split(Constants.DEFAULT_FILE_DELIMITER)))));
        }
        fileContents.setData(fileData);

        /* Return File Contents */
        return fileContents;
    }

    /**
     * Append Header
     * 
     * @param fileContents
     * @return File Contents With Glued Header
     */
    public List<String> appendHeader(List<String> fileContents) {
        LOGGER.info("Start Appending Header...");

        /* Get Column Size */
        String singleLine = fileContents.get(0);
        int columnSize = singleLine.length()
                - singleLine.replaceAll(Constants.DEFAULT_FILE_DELIMITER,
                        Constants.EMPTY_STRING).length();
        LOGGER.info("Column Count: #{}", columnSize);

        /* Stitch Header */
        fileContents.add(0, stitchHeader(1 + columnSize));
        return fileContents;
    }

    /**
     * Stitch Header
     * 
     * @param fileContents
     * @param columnSize
     * @return Glued Header
     */
    private String stitchHeader(final int columnSize) {
        LOGGER.info("Stitching Header...");
        StringBuilder builder = new StringBuilder();

        /* Stitch Header */
        for (int i = 0; i < columnSize; i++) {
            builder.append(Constants.DEFAULT_COLUMN_NAME_PREFIX).append(i)
                    .append(Constants.DEFAULT_FILE_DELIMITER);
        }

        /* Return Stitched Output */
        return new StringBuilder()
                .append(builder.substring(0, builder.length() - 1))
                .append(Constants.DEFAULT_FILE_END_LINE_DELIMITER).toString();
    }

    /**
     * @return the reader
     */
    public UkubukaReader getReader() {
        return reader;
    }
}
