package com.ukubuka.core.model;

import java.util.List;

/**
 * File Contents
 * 
 * @author agrawroh
 * @version v1.0
 */
public class FileContents {

    private List<String> header;
    private List<List<String>> data;

    /**
     * @return the header
     */
    public List<String> getHeader() {
        return header;
    }

    /**
     * @param header
     *            the header to set
     */
    public void setHeader(List<String> header) {
        this.header = header;
    }

    /**
     * @return the data
     */
    public List<List<String>> getData() {
        return data;
    }

    /**
     * @param data
     *            the data to set
     */
    public void setData(List<List<String>> data) {
        this.data = data;
    }
}
