package com.github.doiteasy.easyboot.common.exceptions;


import com.github.doiteasy.easyboot.common.result.ResultCodeEnum;
import org.apache.commons.lang3.ArrayUtils;

public abstract class EasyBootException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private Integer errorCode;

    public EasyBootException(String message) {
        super(message);
    }

    public EasyBootException(String message, Throwable cause) {
        super(message, cause);
    }

    public EasyBootException(Integer errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public EasyBootException(ResultCodeEnum codeEnum) {
        super(codeEnum.getMessage());
        this.errorCode = codeEnum.getCode();
    }

    public EasyBootException(ResultCodeEnum codeEnum, Throwable e) {
        super(codeEnum.getMessage(), e);
        this.errorCode = codeEnum.getCode();
    }

    public Integer getErrorCode() {
        return errorCode;
    }
}
