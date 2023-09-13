package com.gavinjin.smartbibackend.util.exception;

import com.gavinjin.smartbibackend.util.common.ErrorCode;

/**
 * Customized exception during business logic
 */
public class BusinessException extends RuntimeException {
    /**
     * error code
     */
    private final int code;

    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
    }

    public BusinessException(ErrorCode errorCode, String message) {
        super(message);
        this.code = errorCode.getCode();
    }
}
