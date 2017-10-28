package com.ukubuka.core.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * File Contents
 * 
 * @author agrawroh
 * @version v1.0
 */
public class FileContents {

    private List<String> header;
    private List<FileRecord> data;
    private Map<String, Object> aggregations = new HashMap<>();

    /**
     * @return the aggregations
     */
    public Map<String, Object> getAggregations() {
        return aggregations;
    }

    /**
     * @param aggregations the aggregations to set
     */
    public void setAggregations(Map<String, Object> aggregations) {
        this.aggregations = aggregations;
    }

    /***************************** Default Constructor ****************************/
    public FileContents() {
        /* Do Nothing */
    }

    /****************************** Copy Constructor ******************************/
    public FileContents(List<String> header, List<FileRecord> data) {
        this.header = header;
        this.data = data;
    }

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
    public List<FileRecord> getData() {
        return data;
    }

    /**
     * @param data
     *            the data to set
     */
    public void setData(List<FileRecord> data) {
        this.data = data;
    }
}
