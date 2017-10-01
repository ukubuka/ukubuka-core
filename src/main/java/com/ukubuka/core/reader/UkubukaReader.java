package com.ukubuka.core.reader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.ukubuka.core.exception.ReaderException;
import com.ukubuka.core.utilities.Constants;

/**
 * Ukubuka Reader
 * 
 * @author agrawroh
 * @version v1.0
 */
@Component
public class UkubukaReader {

    /**
     * Read File
     * 
     * @param completeFileName
     * @return File Lines
     * @throws ReaderException
     */
    public List<String> readFile(final String completeFileName,
            final String fileEncoding, final String endLineDelimiter)
            throws ReaderException {
        return new ArrayList<>(
                Arrays.asList(readFileAsString(completeFileName, fileEncoding)
                        .split(StringUtils.isEmpty(endLineDelimiter) ? Constants.DEFAULT_FILE_END_LINE_DELIMITER
                                : endLineDelimiter)));
    }

    /**
     * Read File
     * 
     * @param completeFileName
     * @return File Lines
     * @throws ReaderException
     */
    public String readFileAsString(final String completeFileName,
            final String fileEncoding) throws ReaderException {
        try {
            return FileUtils
                    .readFileToString(
                            new File(completeFileName),
                            StringUtils.isEmpty(fileEncoding) ? Constants.DEFAULT_FILE_ENCODING
                                    : fileEncoding);
        } catch (IOException ex) {
            throw new ReaderException(ex);
        }
    }
}
