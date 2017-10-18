package com.ukubuka.core.model;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Ukubuka Schema Model
 * 
 * @author agrawroh
 * @version v1.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class UkubukaSchema {

    @JsonProperty("extract")
    private List<Extract> extracts;

    @JsonProperty("transform")
    private List<Transform> transforms;

    @JsonProperty("load")
    private List<Load> loads;

    /**
     * Extract
     * 
     * @author agrawroh
     * @version v1.0
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Extract {
        @JsonProperty("type")
        private SupportedFileType type;

        @JsonProperty("location")
        private String location;

        @JsonProperty("flags")
        private Map<String, Object> flags;

        /**
         * @return the type
         */
        public SupportedFileType getType() {
            return type;
        }

        /**
         * @param type
         *            the type to set
         */
        public void setType(SupportedFileType type) {
            this.type = type;
        }

        /**
         * @return the location
         */
        public String getLocation() {
            return location;
        }

        /**
         * @param location
         *            the location to set
         */
        public void setLocation(String location) {
            this.location = location;
        }

        /**
         * @return the flags
         */
        public Map<String, Object> getFlags() {
            return flags;
        }

        /**
         * @param flags
         *            the flags to set
         */
        public void setFlags(Map<String, Object> flags) {
            this.flags = flags;
        }
    }

    /**
     * Transform
     * 
     * @author agrawroh
     * @version v1.0
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Transform {

    }

    /**
     * Load
     * 
     * @author agrawroh
     * @version v1.0
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Load {

    }

    /**
     * @return the extracts
     */
    public List<Extract> getExtracts() {
        return extracts;
    }

    /**
     * @param extracts
     *            the extracts to set
     */
    public void setExtracts(List<Extract> extracts) {
        this.extracts = extracts;
    }

    /**
     * @return the transforms
     */
    public List<Transform> getTransforms() {
        return transforms;
    }

    /**
     * @param transforms
     *            the transforms to set
     */
    public void setTransforms(List<Transform> transforms) {
        this.transforms = transforms;
    }

    /**
     * @return the loads
     */
    public List<Load> getLoads() {
        return loads;
    }

    /**
     * @param loads
     *            the loads to set
     */
    public void setLoads(List<Load> loads) {
        this.loads = loads;
    }
}
