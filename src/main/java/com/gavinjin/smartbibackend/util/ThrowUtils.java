package com.gavinjin.smartbibackend.util;

import com.gavinjin.smartbibackend.util.common.ErrorCode;
import com.gavinjin.smartbibackend.util.exception.BusinessException;

/**
 * Utils to throw errors
 */
public class ThrowUtils {
    /**
     * Throw if condition is satisfied
     *
     * @param condition
     * @param runtimeException
     */
    public static void throwIf(boolean condition, RuntimeException runtimeException) {
        if (condition) {
            throw runtimeException;
        }
    }

    /**
     * Throw if condition is satisfied
     *
     * @param condition
     * @param errorCode
     */
    public static void throwIf(boolean condition, ErrorCode errorCode) {
        throwIf(condition, new BusinessException(errorCode));
    }

    /**
     * Throw if condition is satisfied
     *
     * @param condition
     * @param errorCode
     * @param message
     */
    public static void throwIf(boolean condition, ErrorCode errorCode, String message) {
        throwIf(condition, new BusinessException(errorCode, message));
    }
}
