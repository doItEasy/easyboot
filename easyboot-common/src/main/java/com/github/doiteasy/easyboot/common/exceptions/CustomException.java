package com.github.doiteasy.easyboot.common.exceptions;


public abstract class CustomException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private String errorCode;

    public CustomException(String errorMessage) {
        super(errorMessage);
    }

    public CustomException(String message, Throwable cause) {
        super(message, cause);
    }

    public CustomException(String errorCode, String errorMessage) {
        super(errorMessage);
        this.errorCode = errorCode;
    }


    public String getErrorCode() {
        return errorCode;
    }

    public CustomException setErrorCode(String errorCode) {
        this.errorCode = errorCode;
        return this;
    }
    
}
