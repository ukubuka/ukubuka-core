package com.ukubuka.core.utilities;

import java.util.ArrayList;
import java.util.List;

/**
 * Utilities
 * 
 * @author agrawroh
 * @version v1.0
 */
public final class Utilities {

    /********** Private Constructor **********/
    private Utilities() {
        /* Do Nothing */
    }

    /**
     * Extract Delimiter Indices
     * 
     * @param header
     * @param delimiter
     * @return List<Integer> Indices
     */
    public static List<Integer> extractDelimiterIndices(final String header,
            final char delimiter) {
        List<Integer> indices = new ArrayList<>();
        char[] inputTape = header.toCharArray();
        boolean quote = false;
        for (int i = 0; i < inputTape.length; i++) {
            if (inputTape[i] == Constants.COLUMN_ENCOLSING_QUOTE.charAt(0)) {
                quote = !quote;
            } else if (inputTape[i] == delimiter && !quote) {
                indices.add(i);
            }
        }
        return indices;
    }

    /**
     * Apply New Delimiter
     * 
     * @param fileContents
     * @param indices
     * @param newDelimiter
     */
    public static void applyNewDelimiter(List<String> fileContents,
            final String oldDelimiter, final String newDelimiter) {
        for (int i = 0; i < fileContents.size(); i++) {
            String dataRow = fileContents.get(i);
            List<Integer> indices = extractDelimiterIndices(dataRow,
                    oldDelimiter.charAt(0));
            StringBuilder builder = new StringBuilder()
                    .append(dataRow.charAt(0));
            int previousIndex = 0;
            for (final int index : indices) {
                builder.append(dataRow.substring(1, index - previousIndex))
                        .append(newDelimiter);
                dataRow = dataRow.substring(index - previousIndex);
                previousIndex = index;
            }
            String output = builder.append(dataRow.substring(1))
                    .append(newDelimiter).toString();
            fileContents.set(i, output.substring(0,
                    output.length() - newDelimiter.length()));
        }
    }
}
