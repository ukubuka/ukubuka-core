package com.ukubuka.core.parser.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.ukubuka.core.exception.ParserException;
import com.ukubuka.core.exception.ReaderException;
import com.ukubuka.core.model.ExtractFlags;
import com.ukubuka.core.model.FileContents;
import com.ukubuka.core.model.SupportedSource;
import com.ukubuka.core.parser.UkubukaBaseParser;
import com.ukubuka.core.parser.UkubukaParser;
import com.ukubuka.core.utilities.Constants;

/**
 * Ukubuka XML Parser
 * 
 * @author aswinv
 * @version v1.0
 */
@Component("UkubukaXMLParser")
public class UkubukaXMLParser extends UkubukaBaseParser
        implements UkubukaParser {

    /************************************ Logger Instance ***********************************/
    private static final Logger LOGGER = LoggerFactory
            .getLogger(UkubukaXMLParser.class);

    /**
     * Parse File
     */
    @Override
    public FileContents parseFile(final String completeFileName,
            Map<String, Object> flags) throws ParserException {
        LOGGER.info("Parsing XML File - Location: {} | Flags: {}",
                completeFileName, flags);
        return super.getFileContents(readWithOptions(completeFileName, flags));
    }

    /**
     * Get Parser Information
     */
    @Override
    public String getParserInfo() {
        return this.getClass().getSimpleName();
    }

    /**
     * Read With Options
     * 
     * @param completeFileName
     * @return File Content
     * @throws ParserException
     */
    private List<String> readWithOptions(final String completeFileName,
            Map<String, Object> flags) throws ParserException {
        boolean withHeader = null == flags
                .get(ExtractFlags.FILE_CONTAINS_HEADER.getFlag())
                || (boolean) flags
                        .get(ExtractFlags.FILE_CONTAINS_HEADER.getFlag());
        SupportedSource source = null == flags
                .get(ExtractFlags.SOURCE.getFlag()) ? SupportedSource.FILE
                        : SupportedSource.getSource((String) flags
                                .get(ExtractFlags.SOURCE.getFlag()));
        List<String> fileContents = readWithOptions(source, completeFileName);
        return withHeader ? fileContents : super.appendHeader(fileContents);
    }

    /**
     * Read With Options
     * 
     * @param completeFileName
     *            =* @return File Content
     * @throws ParserException
     */
    private List<String> readWithOptions(final SupportedSource source,
            final String completeFileName) throws ParserException {
        try {
            String[] fileContents = extractDataFromStream(super.getReader()
                    .readFileAsStream(source, completeFileName))
                            .split(Constants.DEFAULT_FILE_END_LINE_DELIMITER);
            return new ArrayList<>(Arrays.asList(fileContents));
        } catch (ReaderException ex) {
            throw new ParserException(ex);
        }
    }

    private String extractDataFromStream(XMLStreamReader streamReader)
            throws ReaderException {
        StringBuilder axisPoints = new StringBuilder();
        StringBuilder axisHeaders = new StringBuilder();

        List<Integer> childStack = new ArrayList<>();
        String grandParent = null, parent = null;
        boolean firstParent = true;

        try {
            do {
                streamReader.nextTag();
                if (streamReader.isStartElement()) {
                    if (grandParent == null) {
                        grandParent = streamReader.getLocalName();
                        childStack.add(0, 1);
                    } else if (parent == null) {
                        childStack.add(0, 2);
                        parent = streamReader.getLocalName();
                    } else {
                        if (firstParent) {
                            axisHeaders.append(streamReader.getLocalName());
                            axisHeaders
                                    .append(Constants.DEFAULT_FILE_DELIMITER);
                        }
                        axisPoints.append(streamReader.getElementText());
                        axisPoints.append(Constants.DEFAULT_FILE_DELIMITER);
                    }
                } else if (streamReader.isEndElement()) {
                    if (parent != null) {
                        axisPoints.append(
                                Constants.DEFAULT_FILE_END_LINE_DELIMITER);
                        firstParent = false;
                    }
                    parent = null;
                    childStack.remove(0);
                }
            } while (streamReader.hasNext()
                    && !CollectionUtils.isEmpty(childStack));
        } catch (XMLStreamException ex) {
            throw new ReaderException(ex);
        }

        return axisHeaders
                .replace(
                        axisHeaders
                                .lastIndexOf(Constants.DEFAULT_FILE_DELIMITER),
                        axisHeaders.length(),
                        Constants.DEFAULT_FILE_END_LINE_DELIMITER)
                .append(axisPoints.toString()
                        .replaceAll(Constants.DEFAULT_FILE_DELIMITER
                                + Constants.DEFAULT_FILE_END_LINE_DELIMITER,
                                Constants.DEFAULT_FILE_END_LINE_DELIMITER))
                .toString();
    }
}
