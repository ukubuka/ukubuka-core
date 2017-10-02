package com.ukubuka.core.model;

/**
 * Extract Flags
 * 
 * @author agrawroh
 * @version v1.0
 */
public enum ExtractFlags {
    FILE_ENCODING("fileEncoding"), FILE_END_LINE_DELIMITER("endLineDelimiter"), FILE_DELIMITER(
            "fileDelimiter"), FILE_CONTAINS_HEADER("withHeader"), SOURCE(
            "source");

    /* File Type */
    private String flag;

    /********** Private Constructor **********/
    private ExtractFlags(final String flag) {
        this.flag = flag;
    }

    /**
     * @return the flag
     */
    public String getFlag() {
        return flag;
    }
}
