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
    public static final String DEFAULT_FILE_DELIMITER = ",";
    public static final String DEFAULT_FILE_END_LINE_DELIMITER = "\n";

    /********************************** Parser Constants **********************************/
    public static final String EMPTY_STRING = "";
    public static final String DEFAULT_COLUMN_NAME_PREFIX = "column_";
    public static final String DELIMITER_REPLACE_REGEX_START = "(?!\\B\"[^\"]*)";
    public static final String DELIMITER_REPLACE_REGEX_END = "(?![^\"]*\"\\B)";

    /******************************* Transformer Constants ********************************/
    public static final String COLUMN_ENCOLSING_QUOTE = "\"";
}
