package com.ukubuka.core.model;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * Supported File Type
 * 
 * @author agrawroh
 * @version v1.0
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum SupportedFileType {

    XML("xml"), CSV("csv"), JSON("json");

    /* File Type */
    private String fileType;

    /********** Private Constructor **********/
    private SupportedFileType(final String fileType) {
        this.fileType = fileType;
    }

    /**
     * @return the fileType
     */
    public String getFileType() {
        return fileType;
    }
}
