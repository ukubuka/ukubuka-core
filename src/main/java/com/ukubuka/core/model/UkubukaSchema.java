package com.ukubuka.core.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Ukubuka Schema Model
 * 
 * @author agrawroh
 * @version v1.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class UkubukaSchema {

    private List<Extract> extracts;
    private List<Transform> transforms;
    private List<Load> loads;

    /**
     * Extract
     * 
     * @author agrawroh
     * @version v1.0
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Extract {
        private SupportedSource source;
        private SupportedFileType type;
        private String location;
        private List<Flag> flags;

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Flag {
            private String key;
            private String value;

            /**
             * @return the key
             */
            public String getKey() {
                return key;
            }

            /**
             * @param key
             *            the key to set
             */
            public void setKey(String key) {
                this.key = key;
            }

            /**
             * @return the value
             */
            public String getValue() {
                return value;
            }

            /**
             * @param value
             *            the value to set
             */
            public void setValue(String value) {
                this.value = value;
            }
        }

        /**
         * @return the source
         */
        public SupportedSource getSource() {
            return source;
        }

        /**
         * @param source
         *            the source to set
         */
        public void setSource(SupportedSource source) {
            this.source = source;
        }

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
        public List<Flag> getFlags() {
            return flags;
        }

        /**
         * @param flags
         *            the flags to set
         */
        public void setFlags(List<Flag> flags) {
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
