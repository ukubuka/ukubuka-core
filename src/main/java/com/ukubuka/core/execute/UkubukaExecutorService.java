package com.ukubuka.core.execute;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ukubuka.core.exception.ParserException;
import com.ukubuka.core.exception.TransformException;
import com.ukubuka.core.exception.WriterException;
import com.ukubuka.core.model.FileContents;
import com.ukubuka.core.model.UkubukaSchema;
import com.ukubuka.core.operations.extract.UkubukaExtractor;
import com.ukubuka.core.operations.load.UkubukaLoader;
import com.ukubuka.core.operations.transform.UkubukaTransformer;
import com.ukubuka.core.schema.UkubukaSchemaReader;

/**
 * Ukubuka Executor Service
 * 
 * @author agrawroh
 * @version v1.0
 */
@Service
public class UkubukaExecutorService {

    /************************************ Logger Instance ***********************************/
    private static final Logger LOGGER = LoggerFactory
            .getLogger(UkubukaExecutorService.class);

    /********************************* Dependency Injections ********************************/
    @Autowired
    private UkubukaSchemaReader ukubukaSchemaReader;

    @Autowired
    private UkubukaExtractor ukubukaExtractor;

    @Autowired
    private UkubukaTransformer ukubukaTransformer;

    @Autowired
    private UkubukaLoader ukubukaLoader;

    /**
     * Execute Ukubuka
     * 
     * @param ukubukaSchemaFile
     * @throws ParserException
     * @throws TransformException
     * @throws WriterException
     */
    public void execute(final String ukubukaSchemaFile)
            throws ParserException, TransformException, WriterException {
        /* Create An In-Memory Data Store */
        Map<String, FileContents> dataFiles = new HashMap<>();

        /* Read File*/
        LOGGER.info("Reading Ukubuka Schema...");
        UkubukaSchema ukubukaSchema = ukubukaSchemaReader
                .readSchema(ukubukaSchemaFile);

        /* Perform Extracts */
        LOGGER.info("Performing Extract(s)...");
        ukubukaExtractor.performOperations(dataFiles,
                ukubukaSchema.getExtracts());

        /* Perform Transformations */
        LOGGER.info("Performing Transformation(s)...");
        ukubukaTransformer.performOperations(dataFiles,
                ukubukaSchema.getTransforms());

        /* Perform Load */
        LOGGER.info("Performing Load(s)...");
        ukubukaLoader.performOperations(dataFiles, ukubukaSchema.getLoads());
    }
}
