package com.ukubuka.core.model;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * Supported Source
 * 
 * @author agrawroh
 * @version v1.0
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum SupportedSource {

    FILE("file"), URL("url");

    /* Source */
    private String source;

    /********** Private Constructor **********/
    private SupportedSource(final String source) {
        this.source = source;
    }

    /**
     * @return the source
     */
    public String getSource() {
        return source;
    }

    /**
     * Get Source
     * 
     * @param source
     * @return SupportedSource
     */
    public static SupportedSource getSource(final String source) {
        for (final SupportedSource supportedSource : SupportedSource.values()) {
            if (supportedSource.getSource().equals(source)) {
                return supportedSource;
            }
        }
        return SupportedSource.FILE;
    }
}
