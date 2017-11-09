package com.ukubuka.core.scripts;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ukubuka.core.exception.ReaderException;
import com.ukubuka.core.launcher.UkubukaLauncher;
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
     * @throws IOException
     */
    public String createHTML(final String scriptsDirectoryRoot)
            throws ReaderException, IOException {
        StringBuilder htmlFileBuilder = new StringBuilder();
        htmlFileBuilder.append(getFileContents(UkubukaLauncher.getAppContext()
                .getResource(
                        scriptsDirectoryRoot + Constants.BLANK_TEMPLATE_TAG)
                .getFile().getAbsolutePath())
                        .replace(Constants.HEAD_TAG,
                                readScripts(scriptsDirectoryRoot
                                        + Constants.INCLUDE_TAG))
                        .replace(Constants.BODY_TAG,
                                getFileContents(UkubukaLauncher.getAppContext()
                                        .getResource(scriptsDirectoryRoot
                                                + Constants.HTML_BODY_TAG)
                                        .getFile().getAbsolutePath())));
        return htmlFileBuilder.toString();
    }

    /**
     * Read Scripts
     * 
     * @param scriptsDirectoryRoot
     * @return htmlFileContents
     * @throws ReaderException
     * @throws IOException
     */
    private String readScripts(final String scriptsDirectoryRoot)
            throws ReaderException, IOException {
        StringBuilder htmlFileBuilder = new StringBuilder();
        LOGGER.info("Reading Directory: {}", scriptsDirectoryRoot);
        for (final File file : UkubukaLauncher.getAppContext()
                .getResource(scriptsDirectoryRoot).getFile().listFiles()) {
            if (file.isDirectory()) {
                LOGGER.info("Reading Resource Directory: {}", file.getName());
                htmlFileBuilder.append(readScript(
                        scriptsDirectoryRoot + Constants.FORWARD_SLASH
                                + file.getName(),
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
     * @throws IOException
     */
    private String readScript(final String resourceDirectoryPath,
            final String htmlTag) throws ReaderException, IOException {
        StringBuilder builder = new StringBuilder();
        for (final File file : UkubukaLauncher.getAppContext()
                .getResource(resourceDirectoryPath).getFile().listFiles()) {
            LOGGER.info("Reading Resource: {}", file.getAbsolutePath());
            builder.append(file.isDirectory()
                    ? readScript(resourceDirectoryPath + Constants.FORWARD_SLASH
                            + file.getName(), htmlTag)
                    : htmlTag.replace(Constants.CONTENT_TAG,
                            getFileContents(UkubukaLauncher.getAppContext()
                                    .getResource(resourceDirectoryPath
                                            + Constants.FORWARD_SLASH
                                            + file.getName())
                                    .getFile().getAbsolutePath())));
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
        return reader.readFileAsString(SupportedSource.FILE, completeFileName,
                Constants.DEFAULT_FILE_ENCODING);
    }
}
