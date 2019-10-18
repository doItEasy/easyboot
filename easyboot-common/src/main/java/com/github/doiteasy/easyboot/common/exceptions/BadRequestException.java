package com.github.doiteasy.easyboot.common.exceptions;

import com.github.doiteasy.easyboot.common.result.ResultCodeEnum;


/**
 * 400打头的客户端异常
 */
public class BadRequestException extends EasyBootException {

    private static final long serialVersionUID = 1L;


    public BadRequestException(String message) {
        super(message);
    }
    public BadRequestException(Integer errorCode, String message) {
        super(errorCode,message);
    }

    public BadRequestException(ResultCodeEnum codeEnum) {
        super(codeEnum);
    }

    public BadRequestException(ResultCodeEnum codeEnum, Throwable e) {
        super(codeEnum, e);
    }

}
