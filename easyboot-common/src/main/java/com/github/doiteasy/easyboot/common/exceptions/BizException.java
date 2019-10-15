package com.github.doiteasy.easyboot.common.exceptions;


import static com.github.doiteasy.easyboot.common.exceptions.BizExceptionConstants.KEY_ERR_BIZ;

/**
 * 异常
 * 4XX
 * 5XX
 *
 * @author feixiangming
 */
public class BizException extends RuntimeException {
    private final int code;
    private final String errorKey;

    public BizException(String message) {
        super(message);
        this.errorKey = KEY_ERR_BIZ;
        this.code = BizExceptionConstants.CLIENT_ERROR_CODE;
    }

    public BizException(String errorKey, String message) {
        super(message);
        this.errorKey = errorKey;
        this.code = BizExceptionConstants.CLIENT_ERROR_CODE;
    }

    public BizException(String message, int code) {
        super(message);
        this.errorKey = KEY_ERR_BIZ;
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public String getErrorKey() {
        return errorKey;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [code=" + getCode() + ", message=" + getMessage() + "]";
    }
}
