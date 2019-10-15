package com.github.doiteasy.easyboot.common.exceptions;

public class BadRequestException extends CustomException {

    private static final long serialVersionUID = 1L;


    public BadRequestException(String errCode, String errorMessage) {
        super(errCode, errorMessage);
    }

}
