package com.ukubuka.core.operations.visualize;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ukubuka.core.exception.PipelineException;
import com.ukubuka.core.exception.ReaderException;
import com.ukubuka.core.exception.WriterException;
import com.ukubuka.core.model.FileContents;
import com.ukubuka.core.model.UkubukaSchema;
import com.ukubuka.core.model.UkubukaSchema.Visualization;
import com.ukubuka.core.operations.UkubukaOperations;
import com.ukubuka.core.scripts.UkubukaScriptsReader;
import com.ukubuka.core.utilities.Constants;
import com.ukubuka.core.writer.UkubukaWriter;

/**
 * Ukubuka Visualizer
 * 
 * @author agrawroh
 * @version v1.0
 */
@Component("UkubukaVisualizer")
public class UkubukaVisualizer implements UkubukaOperations {

    /************************************ Logger Instance ***********************************/
    private static final Logger LOGGER = LoggerFactory
            .getLogger(UkubukaVisualizer.class);

    /******************************** Dependency Injections *********************************/
    @Autowired
    private UkubukaScriptsReader scriptsReader;

    @Autowired
    private UkubukaWriter writer;

    /**
     * Perform Operations
     * 
     * @param dataFiles
     * @param schema
     * @throws PipelineException
     */
    public void performOperations(Map<String, FileContents> dataFiles,
            final UkubukaSchema schema) throws PipelineException {
        try {
            performOperations(dataFiles, schema.getVisualizations());
        } catch (ReaderException | WriterException | IOException ex) {
            throw new PipelineException(ex);
        }
    }

    /**
     * Perform Operations
     * 
     * @param fileHeader
     * @param operationsList
     * @param fileRecords
     * @throws ReaderException
     * @throws WriterException
     * @throws IOException
     */
    public Map<String, FileContents> performOperations(
            Map<String, FileContents> dataFiles,
            final List<Visualization> visualizations)
            throws ReaderException, WriterException, IOException {
        /* Iterate Operations */
        for (final Visualization visualization : visualizations) {
            LOGGER.info("Creating Visualization With Hash: HC{}",
                    visualization.hashCode());
            String htmlPageContent = createHTMLPage(visualization,
                    dataFiles.get(visualization.getId()));
            LOGGER.info("HTML Content: {}", htmlPageContent);

            /* Write File */
            if (StringUtils.isNotEmpty(visualization.getLocation())) {
                LOGGER.info("Writing File...");
                LOGGER.info("Location: {}", visualization.getLocation());
                writer.writeFile(visualization.getLocation(), htmlPageContent);
            }
        }
        return dataFiles;
    }

    /**
     * Create HTML Page
     * 
     * @param visualization
     * @param fileContents
     * @return htmlPageContent
     * @throws ReaderException
     * @throws WriterException
     * @throws IOException
     */
    private String createHTMLPage(final Visualization visualization,
            final FileContents fileContents)
            throws ReaderException, WriterException, IOException {
        return scriptsReader
                .createHTML(Constants.CLASSPATH + Constants.SCRIPTS_TAG
                        + Constants.FORWARD_SLASH + visualization.getType()
                        + Constants.FORWARD_SLASH)
                .replace(Constants.WIDTH_TAG,
                        visualization.getFlags().getWidth())
                .replace(Constants.HEIGHT_TAG,
                        visualization.getFlags().getHeight())
                .replace(Constants.OPTION_TAG,
                        visualization.getFlags().getOptions())
                .replace(
                        Constants.DATA_TAG, writer
                                .prettyPrint(writer
                                        .writeJSON(fileContents.getHeader(),
                                                fileContents.getData())
                                        .toString()));
    }
}
