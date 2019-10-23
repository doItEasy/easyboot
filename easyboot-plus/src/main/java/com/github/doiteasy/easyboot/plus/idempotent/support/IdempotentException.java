package com.github.doiteasy.easyboot.plus.idempotent.support;

import com.github.doiteasy.easyboot.common.exceptions.EasyBootException;
import com.github.doiteasy.easyboot.common.result.ResultCodeEnum;

public class IdempotentException extends EasyBootException {

    public IdempotentException(String message) {
        super(message);
    }

    public IdempotentException(Integer errorCode, String message) {
        super(errorCode, message);
    }

    public IdempotentException(ResultCodeEnum codeEnum) {
        super(codeEnum);
    }



}
