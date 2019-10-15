package com.github.doiteasy.easyboot.common.exceptions;

public class InternalServerException extends CustomException {

    private static final long serialVersionUID = 1L;

    public InternalServerException(String errMessage) {
        super("unknown", errMessage);
    }

    public InternalServerException(String errMessage, Throwable throwable) {
        super(errMessage, throwable);
    }

}
