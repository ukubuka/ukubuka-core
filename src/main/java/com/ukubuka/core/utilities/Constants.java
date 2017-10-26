package com.ukubuka.core.utilities;

/**
 * Application Constants
 * 
 * @author agrawroh
 * @version v1.0
 */
public final class Constants {

    /********************************** Reader Constants **********************************/
    public static final String DEFAULT_FILE_ENCODING = "UTF-8";
    public static final String DEFAULT_FILE_DELIMITER = "~`~";
    public static final String COMMON_FILE_DELIMITER = ",";
    public static final String DEFAULT_FILE_END_LINE_DELIMITER = "\n";

    /********************************** Parser Constants **********************************/
    public static final String EMPTY_STRING = "";
    public static final String DEFAULT_COLUMN_NAME_PREFIX = "column_";

    /******************************* Transformer Constants ********************************/
    public static final String COLUMN_ENCOLSING_QUOTE = "\"";
    public static final String SHORTCUT_MAP_DELIMITER = "=";

    /****************************** Scripts Reader Constants ******************************/
    public static final String CONTENT_TAG = "$$$CONTENT$$$";
    public static final String FORWARD_SLASH = "/";
    public static final String OPENING_BRACKET = "<";
    public static final String CLOSING_BRACKET = ">";

    /******************************** Private Constructor *********************************/
    private Constants() {
        /* Do Nothing */
    }
}
