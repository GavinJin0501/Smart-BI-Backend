package com.gavinjin.smartbibackend.util;

import org.apache.commons.lang3.StringUtils;

/**
 * Utils for sql
 */
public class SqlUtils {
    /**
     * Validate sort filed (prevent sql injection)
     * @param sortField
     * @return
     */
    public static boolean validSortField(String sortField) {
        if (StringUtils.isBlank(sortField)) {
            return false;
        }
        return !StringUtils.containsAny(sortField, "=", "(", ")", " ");
    }
}
