package com.github.doiteasy.easyboot.common.exceptions;

import com.github.doiteasy.easyboot.common.result.ResultCodeEnum;
import org.apache.commons.lang3.ArrayUtils;


/**
 * 400打头的客户端异常
 */
public class BadRequestException extends EasyBootException {

    private static final long serialVersionUID = 1L;


    public BadRequestException(String message) {
        super(400,message);
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

    public static BadRequestException with(ResultCodeEnum codeEnum, Object... messages) {
        String message = codeEnum.getMessage();
        if (ArrayUtils.isNotEmpty(messages)) {
            message = String.format(message, messages);
        }
        return new BadRequestException(codeEnum.getCode(),message);
    }
}
