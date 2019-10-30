package com.github.doiteasy.easyboot.common.exceptions;

import com.github.doiteasy.easyboot.common.result.ResultCodeEnum;
import org.apache.commons.lang3.ArrayUtils;


/**
 * 400打头的客户端异常
 */
public class ClientRequestException extends EasyBootException {

    private static final long serialVersionUID = 1L;

    public ClientRequestException(String message) {
        super(400,message);
    }

    public ClientRequestException(String message, Throwable cause) {
        super(400,message,cause);
    }

    public ClientRequestException(Integer errorCode, String message) {
        super(errorCode,message);
    }

    public ClientRequestException(Integer errorCode, String message, Throwable cause) {
        super(errorCode,message,cause);
    }

    public static ClientRequestException with(ResultCodeEnum codeEnum, Object... messages) {
        String message = codeEnum.getMessage();
        if (ArrayUtils.isNotEmpty(messages)) {
            message = String.format(message, messages);
        }
        return new ClientRequestException(codeEnum.getCode(),message);
    }

    public static ClientRequestException with(ResultCodeEnum codeEnum, Throwable cause, Object... messages) {
        String message = codeEnum.getMessage();
        if (ArrayUtils.isNotEmpty(messages)) {
            message = String.format(message, messages);
        }
        return new ClientRequestException(codeEnum.getCode(),message,cause);
    }
}
