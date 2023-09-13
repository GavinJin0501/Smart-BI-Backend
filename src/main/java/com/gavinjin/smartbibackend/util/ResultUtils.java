package com.gavinjin.smartbibackend.util;

import com.gavinjin.smartbibackend.util.common.BaseResponse;
import com.gavinjin.smartbibackend.util.common.ErrorCode;

/**
 * Utils to generate the corresponding response objects
 */
public class ResultUtils {
    /**
     * Success
     *
     * @param data
     * @return
     * @param <T>
     */
    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<>(0, data, "ok");
    }

    /**
     * Error
     *
     * @param errorCode
     * @return
     * @param <T>
     */
    public static <T> BaseResponse<T> error(ErrorCode errorCode) {
        return new BaseResponse<>(errorCode);
    }

    /**
     * Error
     *
     * @param errorCode
     * @param message
     * @return
     * @param <T>
     */
    public static <T> BaseResponse<T> error(int errorCode, String message) {
        return new BaseResponse<>(errorCode, null, message);
    }

    /**
     * Error
     *
     * @param errorCode
     * @param message
     * @return
     */
    public static BaseResponse error(ErrorCode errorCode, String message) {
        return new BaseResponse<>(errorCode.getCode(), null, message);
    }
}
