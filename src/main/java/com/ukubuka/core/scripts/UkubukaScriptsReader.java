package com.ukubuka.core.scripts;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ukubuka.core.exception.ReaderException;
import com.ukubuka.core.model.SupportedSource;
import com.ukubuka.core.operations.visualize.UkubukaVisualizer;
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

    public static void main(String[] args) throws ReaderException {
        System.out.println(new UkubukaScriptsReader()
                .readScripts(UkubukaVisualizer.class.getClassLoader()
                        .getResource("scripts/tau-charts/include").getFile()));
    }

    /**
     * Read Scripts
     * 
     * @param scriptsDirectoryRoot
     * @return Map<HTMLTag, Scripts>
     * @throws ReaderException
     */
    private Map<String, String> readScripts(final String scriptsDirectoryRoot)
            throws ReaderException {
        Map<String, String> scriptsMap = new HashMap<>();
        LOGGER.info("Reading Directory: {}", scriptsDirectoryRoot);
        for (final File file : new File(scriptsDirectoryRoot).listFiles()) {
            if (file.isDirectory()) {
                LOGGER.info("Reading Resource Directory: {}", file.getName());
                scriptsMap.put(file.getName(), readScript(
                        file.getAbsolutePath(),
                        new StringBuilder().append(
                                Constants.DEFAULT_FILE_END_LINE_DELIMITER)
                                .append(Constants.OPENING_BRACKET)
                                .append(file.getName())
                                .append(Constants.CLOSING_BRACKET)
                                .append(Constants.DEFAULT_FILE_END_LINE_DELIMITER)
                                .append(Constants.CONTENT_TAG)
                                .append(Constants.DEFAULT_FILE_END_LINE_DELIMITER)
                                .append(Constants.OPENING_BRACKET)
                                .append(file.getName())
                                .append(Constants.FORWARD_SLASH)
                                .append(Constants.CLOSING_BRACKET)
                                .append(Constants.DEFAULT_FILE_END_LINE_DELIMITER)
                                .toString()));
            }
        }
        return scriptsMap;
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
