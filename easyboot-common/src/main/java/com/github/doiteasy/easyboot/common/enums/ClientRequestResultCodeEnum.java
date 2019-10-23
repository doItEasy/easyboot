package com.github.doiteasy.easyboot.common.enums;


import com.github.doiteasy.easyboot.common.result.ResultCodeEnum;

public enum ClientRequestResultCodeEnum implements ResultCodeEnum {


    PARAM_IS_BLANK(4001,"参数为空"),
    PARAM_IS_INVAILD(4002,"参数无效"),
    PARAM_TYPE_BIND_ERROR(4003,"参数类型错误"),
    PARAM_NOT_COMPLETE(4004,"参数缺失"),

    ;

    ClientRequestResultCodeEnum(Integer code, String message) {
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
