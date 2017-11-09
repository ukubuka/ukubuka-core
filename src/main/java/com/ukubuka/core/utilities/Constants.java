package com.ukubuka.core.utilities;

import java.math.BigDecimal;

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

    /******************************** Evaluator Constants *********************************/
    public static final int DIVISION_OPERATION_PRECISION = 25;
    public static final int DIVISION_OPERATION_ROUNDING = BigDecimal.ROUND_HALF_UP;

    /****************************** Scripts Reader Constants ******************************/
    public static final String CONTENT_TAG = "$$$CONTENT$$$";
    public static final String FORWARD_SLASH = "/";
    public static final String OPENING_BRACKET = "<";
    public static final String CLOSING_BRACKET = ">";

    /******************************** Visualizer Constants ********************************/
    public static final String BLANK_TEMPLATE_TAG = "blank-template.html";
    public static final String HTML_BODY_TAG = "body.html";
    public static final String SCRIPTS_TAG = "scripts";
    public static final String INCLUDE_TAG = "include";
    public static final String DATA_TAG = "$$$DATA$$$";
    public static final String HEAD_TAG = "$$$HEAD$$$";
    public static final String BODY_TAG = "$$$BODY$$$";
    public static final String WIDTH_TAG = "$$$WIDTH$$$";
    public static final String HEIGHT_TAG = "$$$HEIGHT$$$";
    public static final String OPTION_TAG = "$$$OPTION$$$";

    /******************************** Private Constructor *********************************/
    private Constants() {
        /* Do Nothing */
    }
}
