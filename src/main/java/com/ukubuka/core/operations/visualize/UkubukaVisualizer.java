package com.ukubuka.core.operations.visualize;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ukubuka.core.exception.ReaderException;
import com.ukubuka.core.exception.WriterException;
import com.ukubuka.core.model.FileContents;
import com.ukubuka.core.model.SupportedSource;
import com.ukubuka.core.model.UkubukaSchema.Load;
import com.ukubuka.core.reader.UkubukaReader;
import com.ukubuka.core.utilities.Constants;
import com.ukubuka.core.writer.UkubukaWriter;

/**
 * Ukubuka Visualizer
 * 
 * @author agrawroh
 * @version v1.0
 */
@Component
public class UkubukaVisualizer {

    /************************************ Logger Instance ***********************************/
    private static final Logger LOGGER = LoggerFactory
            .getLogger(UkubukaVisualizer.class);

    /******************************** Dependency Injections *********************************/
    @Autowired
    private UkubukaReader reader;

    @Autowired
    private UkubukaWriter writer;

    private static String fin = "";

    public static void main(String[] args) throws ReaderException {
        new UkubukaVisualizer().readScript(UkubukaVisualizer.class
                .getClassLoader().getResource("scripts/tau-charts/include/css")
                .getFile());
        new UkubukaVisualizer().readScript(UkubukaVisualizer.class
                .getClassLoader().getResource("scripts/tau-charts/include/js")
                .getFile());
        System.out.println(fin);
    }

    /**
     * Perform Operations
     * 
     * @param fileHeader
     * @param operationsList
     * @param fileRecords
     * @throws WriterException
     */
    public Map<String, FileContents> performOperations(
            Map<String, FileContents> dataFiles, final List<Load> loads)
            throws WriterException {
        return dataFiles;
    }

    private void readScript(final String completeFileName)
            throws ReaderException {
        File f = new File(completeFileName);
        if (f.exists()) {
            for (final File fl : f.listFiles()) {
                if (!fl.isDirectory()) {
                    fin += ("\n" + "\n" + "\n" + "\n******************"
                            + processFile(fl.getAbsolutePath()));
                } else {
                    readScript(fl.getAbsolutePath());
                }
            }
        }

    }

    private String processFile(final String completeFileName)
            throws ReaderException {
        reader = new UkubukaReader();
        return reader.readFileAsString(SupportedSource.FILE, completeFileName,
                Constants.DEFAULT_FILE_ENCODING);
    }
}
