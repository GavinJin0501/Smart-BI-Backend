package com.gavinjin.smartbibackend.util.common;

import lombok.Getter;

/**
 * Customized error code
 */
@Getter
public enum ErrorCode {
    SUCCESS(0, "ok"),
    PARAMS_ERROR(40000, "Request parameter error"),
    NOT_LOGIN_ERROR(40100, "Not logged in"),
    NO_AUTH_ERROR(40101, "No authority"),
    NOT_FOUND_ERROR(40400, "Request data not found"),
    FORBIDDEN_ERROR(40300, "Forbidden"),
    TOO_MANY_REQUESTS(42900, "Too many requests"),
    SYSTEM_ERROR(50000, "Server internal error"),
    OPERATION_ERROR(50001, "Operation failure");

    /**
     * Status code
     */
    private final int code;

    /**
     * Message
     */
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
