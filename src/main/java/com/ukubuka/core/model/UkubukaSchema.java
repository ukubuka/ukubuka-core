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
        @JsonProperty("id")
        private String id;

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

        /**
         * @return the id
         */
        public String getId() {
            return id;
        }

        /**
         * @param id
         *            the id to set
         */
        public void setId(String id) {
            this.id = id;
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
        @JsonProperty("id")
        private String id;

        @JsonProperty("operations")
        private List<Operations> operations;

        /**
         * @return the id
         */
        public String getId() {
            return id;
        }

        /**
         * @param id
         *            the id to set
         */
        public void setId(String id) {
            this.id = id;
        }

        /**
         * @return the operations
         */
        public List<Operations> getOperations() {
            return operations;
        }

        /**
         * @param operations
         *            the operations to set
         */
        public void setOperations(List<Operations> operations) {
            this.operations = operations;
        }
    }

    /**
     * Operations
     * 
     * @author agrawroh
     * @version v1.0
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Operations {
        @JsonProperty("type")
        private TransformOperation type;

        @JsonProperty("source")
        private String source;

        @JsonProperty("target")
        private String target;

        /**
         * @return the source
         */
        public String getSource() {
            return source;
        }

        /**
         * @param source
         *            the source to set
         */
        public void setSource(String source) {
            this.source = source;
        }

        /**
         * @return the target
         */
        public String getTarget() {
            return target;
        }

        /**
         * @param target
         *            the target to set
         */
        public void setTarget(String target) {
            this.target = target;
        }

        /**
         * @return the type
         */
        public TransformOperation getType() {
            return type;
        }

        /**
         * @param type
         *            the type to set
         */
        public void setType(TransformOperation type) {
            this.type = type;
        }
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
