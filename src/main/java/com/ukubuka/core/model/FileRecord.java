package com.ukubuka.core.model;

import java.util.List;

/**
 * File Record
 * 
 * @author agrawroh
 * @version v1.0
 */
public class FileRecord {

    private List<String> data;

    /***************************** Default Constructor ****************************/
    public FileRecord() {
        /* Do Nothing */
    }

    /****************************** Copy Constructor ******************************/
    public FileRecord(List<String> data) {
        this.data = data;
    }

    /**
     * @return the data
     */
    public List<String> getData() {
        return data;
    }

    /**
     * @param data
     *            the data to set
     */
    public void setData(List<String> data) {
        this.data = data;
    }
}
