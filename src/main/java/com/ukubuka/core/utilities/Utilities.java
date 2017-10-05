package com.ukubuka.core.utilities;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * Utilities
 * 
 * @author agrawroh
 * @version v1.0
 */
public final class Utilities {

    /********** Private Constructor **********/
    private Utilities() {
        /* Do Nothing */
    }

    /**
     * Write File
     * 
     * @param completeFileName
     * @param fileContents
     * @throws IOException
     */
    public static void writeFile(final String completeFileName,
            final String fileContents) throws IOException {
        try (PrintWriter printWriter = new PrintWriter(completeFileName,
                Constants.DEFAULT_FILE_ENCODING)) {
            printWriter.print(fileContents);
            printWriter.close();
        } catch (IOException ex) {
            throw ex;
        }
    }
}
