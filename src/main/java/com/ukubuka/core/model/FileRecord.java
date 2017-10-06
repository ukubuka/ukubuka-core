package com.ukubuka.core.model;

import java.util.List;

import org.springframework.util.CollectionUtils;

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

    /********************************* Override(s) ********************************/
    @Override
    public boolean equals(Object object) {
        if (object == this)
            return true;
        if (!(object instanceof FileRecord)) {
            return false;
        }

        /* Cast FileRecord */
        FileRecord fileRecord = (FileRecord) object;

        /* Empty List */
        if (CollectionUtils.isEmpty(this.getData())
                || CollectionUtils.isEmpty(fileRecord.getData())) {
            return false;
        }

        /* Check Whether Equal */
        return fileRecord.getData().toString()
                .equals(this.getData().toString());
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + data.hashCode();
        result = 41 * result + data.size();
        return result;
    }
}
