package com.ukubuka.core.reader;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.ukubuka.core.exception.ReaderException;
import com.ukubuka.core.model.SupportedSource;
import com.ukubuka.core.utilities.Constants;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

/**
 * Ukubuka Reader
 * 
 * @author agrawroh
 * @version v1.0
 */
@Component
public class UkubukaReader {

    /************************************ Logger Instance ***********************************/
    private static final Logger LOGGER = LoggerFactory
            .getLogger(UkubukaReader.class);

    /**
     * Read File
     * 
     * @param source
     * @param completeFileName
     * @param fileEncoding
     * @param endLineDelimiter
     * @return File Lines
     * @throws ReaderException
     */
    public List<String> readFile(final SupportedSource source,
            final String completeFileName, final String fileEncoding,
            final String endLineDelimiter) throws ReaderException {
        LOGGER.info(
                "Reading File - Source: {} | Location: {} | Encoding: {} | Delimiter: {}",
                source, completeFileName, fileEncoding, endLineDelimiter);
        return new ArrayList<>(Arrays
                .asList(readFileAsString(source, completeFileName, fileEncoding)
                        .split(StringUtils.isEmpty(endLineDelimiter)
                                ? Constants.DEFAULT_FILE_END_LINE_DELIMITER
                                : endLineDelimiter)));
    }

    /**
     * Read File
     * 
     * @param source
     * @param completeFileName
     * @param fileEncoding
     * @return File Lines
     * @throws ReaderException
     */
    public String readFileAsString(final SupportedSource source,
            final String completeFileName, final String fileEncoding)
            throws ReaderException {
        try {
            return FileUtils
                    .readFileToString(
                            source == SupportedSource.URL
                                    ? new File(
                                            new URL(completeFileName).toURI())
                                    : new File(completeFileName),
                            StringUtils.isEmpty(fileEncoding)
                                    ? Constants.DEFAULT_FILE_ENCODING
                                    : fileEncoding);
        } catch (IOException | URISyntaxException
                | IllegalArgumentException ex) {
            throw new ReaderException(ex);
        }
    }

    public String readXMLAsString(final SupportedSource source,
                                   final String completeFileName)
            throws ReaderException {
        try {
            XMLInputFactory inputFactory = XMLInputFactory.newInstance();
            InputStream in = new FileInputStream(source == SupportedSource.URL
                    ? new File(
                    new URL(completeFileName).toURI())
                    : new File(completeFileName));
            XMLStreamReader streamReader = inputFactory.createXMLStreamReader(in);
            StringBuilder axisPoints = new StringBuilder();
            StringBuilder axisHeaders = new StringBuilder();

            Stack<Integer> childStack = new Stack<>();
            String grandParent = null, parent = null;
            boolean firstParent = true;

            do {
                streamReader.nextTag();
                if (streamReader.isStartElement()) {
                    if (grandParent == null) {
                        grandParent = streamReader.getLocalName();
                        childStack.push(1);
                    } else if (parent == null) {
                        childStack.push(2);
                        parent = streamReader.getLocalName();
                    } else {
                        if (firstParent) {
                            axisHeaders.append(streamReader.getLocalName());
                            axisHeaders.append(Constants.DEFAULT_FILE_DELIMITER);
                        }
                        axisPoints.append(streamReader.getElementText());
                        axisPoints.append(Constants.DEFAULT_FILE_DELIMITER);
                    }
                } else if (streamReader.isEndElement()) {
                    if (parent != null) {
                        axisPoints.append(Constants.DEFAULT_FILE_END_LINE_DELIMITER);
                        firstParent = false;
                    }
                    parent = null;
                    childStack.pop();
                }
            } while (streamReader.hasNext() && !childStack.empty());

            return axisHeaders.replace(axisHeaders.lastIndexOf(Constants.DEFAULT_FILE_DELIMITER), axisHeaders.length(), Constants.DEFAULT_FILE_END_LINE_DELIMITER).append(axisPoints.toString().replaceAll(Constants.DEFAULT_FILE_DELIMITER + Constants.DEFAULT_FILE_END_LINE_DELIMITER, Constants.DEFAULT_FILE_END_LINE_DELIMITER)).toString();
        } catch (IOException | URISyntaxException
                | IllegalArgumentException | XMLStreamException ex) {
            throw new ReaderException(ex);
        }
    }
}
