package com.ukubuka.core.model;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * Supported File Types
 * 
 * @author agrawroh
 * @version v1.0
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum UkubukaSchemaTags {

    EXTRACT("extract"), TRANSFORM("transform"), LOAD("load");

    /* Schema Tag Name */
    private String tagName;

    /********** Private Constructor **********/
    private UkubukaSchemaTags(final String tagName) {
        this.tagName = tagName;
    }

    /**
     * @return the tagName
     */
    public String getTagName() {
        return tagName;
    }
}
