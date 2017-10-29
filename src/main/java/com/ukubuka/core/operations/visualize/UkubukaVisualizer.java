package com.ukubuka.core.operations.visualize;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ukubuka.core.exception.PipelineException;
import com.ukubuka.core.exception.ReaderException;
import com.ukubuka.core.model.FileContents;
import com.ukubuka.core.model.UkubukaSchema;
import com.ukubuka.core.model.UkubukaSchema.Visualization;
import com.ukubuka.core.operations.UkubukaOperations;
import com.ukubuka.core.scripts.UkubukaScriptsReader;
import com.ukubuka.core.utilities.Constants;

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
        } catch (ReaderException ex) {
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
     */
    public Map<String, FileContents> performOperations(
            Map<String, FileContents> dataFiles,
            final List<Visualization> visualizations) throws ReaderException {
        /* Iterate Operations */
        for (final Visualization visualization : visualizations) {
            LOGGER.info("Creating Visualization With Hash: HC{}",
                    visualization.hashCode());
            String htmlPageContent = scriptsReader
                    .createHTML(UkubukaVisualizer.class.getClassLoader()
                            .getResource(Constants.SCRIPTS_TAG
                                    + Constants.FORWARD_SLASH
                                    + visualization.getType()
                                    + Constants.FORWARD_SLASH)
                            .getFile())
                    .replace(Constants.WIDTH_TAG,
                            visualization.getFlags().getWidth())
                    .replace(Constants.HEIGHT_TAG,
                            visualization.getFlags().getHeight());
            LOGGER.info("HTML Content: {}", htmlPageContent);
        }
        return dataFiles;
    }
}
