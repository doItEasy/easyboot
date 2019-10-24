package com.github.doiteasy.easyboot.common.enums;


import com.github.doiteasy.easyboot.common.result.ResultCodeEnum;

public enum ServerBizResultCodeEnum implements ResultCodeEnum {


    USER_NOT_LOGIN_IN(5000,"用户未登陆"),
    USER_NOT_EXIST(5001,"用户不存在"),
    USER_LOGIN_ERROR(5002,"用户名或密码错误"),
    USER_FORBIDDEN(5002,"用户名已禁用"),

    ;

    ServerBizResultCodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    private Integer code;
    private String message;


    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
