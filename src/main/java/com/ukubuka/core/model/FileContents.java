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
    private List<FileRecord> data;

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
