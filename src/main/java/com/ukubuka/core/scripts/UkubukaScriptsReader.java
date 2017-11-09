package com.ukubuka.core.scripts;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ukubuka.core.exception.ReaderException;
import com.ukubuka.core.model.SupportedSource;
import com.ukubuka.core.reader.UkubukaReader;
import com.ukubuka.core.utilities.Constants;

/**
 * Ukubuka Scripts Reader
 * 
 * @author agrawroh
 * @version v1.0
 */
@Component
public class UkubukaScriptsReader {

    /************************************ Logger Instance ***********************************/
    private static final Logger LOGGER = LoggerFactory
            .getLogger(UkubukaScriptsReader.class);

    /********************************* Dependency Injections ********************************/
    @Autowired
    private UkubukaReader reader;

    /**
     * Read Scripts
     * 
     * @param scriptsDirectoryRoot
     * @return htmlFileContents
     * @throws ReaderException
     */
    public String createHTML(final String scriptsDirectoryRoot)
            throws ReaderException {
        StringBuilder htmlFileBuilder = new StringBuilder();
        htmlFileBuilder.append(getFileContents(
                new File(scriptsDirectoryRoot + Constants.BLANK_TEMPLATE_TAG)
                        .getAbsolutePath()).replace(
                                Constants.HEAD_TAG,
                                readScripts(scriptsDirectoryRoot
                                        + Constants.INCLUDE_TAG))
                                .replace(Constants.BODY_TAG, getFileContents(
                                        new File(scriptsDirectoryRoot
                                                + Constants.HTML_BODY_TAG)
                                                        .getAbsolutePath())));
        return htmlFileBuilder.toString();
    }

    /**
     * Read Scripts
     * 
     * @param scriptsDirectoryRoot
     * @return htmlFileContents
     * @throws ReaderException
     */
    private String readScripts(final String scriptsDirectoryRoot)
            throws ReaderException {
        StringBuilder htmlFileBuilder = new StringBuilder();
        LOGGER.info("Reading Directory: {}", scriptsDirectoryRoot);
        for (final File file : new File(scriptsDirectoryRoot).listFiles()) {
            if (file.isDirectory()) {
                LOGGER.info("Reading Resource Directory: {}", file.getName());
                htmlFileBuilder.append(readScript(file.getAbsolutePath(),
                        new StringBuilder().append(
                                Constants.DEFAULT_FILE_END_LINE_DELIMITER)
                                .append(Constants.OPENING_BRACKET)
                                .append(file.getName())
                                .append(Constants.CLOSING_BRACKET)
                                .append(Constants.DEFAULT_FILE_END_LINE_DELIMITER)
                                .append(Constants.CONTENT_TAG)
                                .append(Constants.DEFAULT_FILE_END_LINE_DELIMITER)
                                .append(Constants.OPENING_BRACKET)
                                .append(Constants.FORWARD_SLASH)
                                .append(file.getName())
                                .append(Constants.CLOSING_BRACKET)
                                .append(Constants.DEFAULT_FILE_END_LINE_DELIMITER)
                                .toString()));
            }
        }
        return htmlFileBuilder.toString();
    }

    /**
     * Read Script Files
     * 
     * @param resourceDirectoryPath
     * @param htmlTag
     * @return
     * @throws ReaderException
     */
    private String readScript(final String resourceDirectoryPath,
            final String htmlTag) throws ReaderException {
        StringBuilder builder = new StringBuilder();
        for (final File file : new File(resourceDirectoryPath).listFiles()) {
            LOGGER.info("Reading Resource: {}", file.getAbsolutePath());
            builder.append(file.isDirectory()
                    ? readScript(file.getAbsolutePath(), htmlTag)
                    : htmlTag.replace(Constants.CONTENT_TAG,
                            getFileContents(file.getAbsolutePath())));
        }
        return builder.toString();
    }

    /**
     * Get File Contents
     * 
     * @param completeFileName
     * @return fileContents
     * @throws ReaderException
     */
    private String getFileContents(final String completeFileName)
            throws ReaderException {
        reader = new UkubukaReader();
        return reader.readFileAsString(SupportedSource.FILE, completeFileName,
                Constants.DEFAULT_FILE_ENCODING);
    }
}
