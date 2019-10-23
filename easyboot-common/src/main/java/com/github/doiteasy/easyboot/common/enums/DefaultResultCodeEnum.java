package com.github.doiteasy.easyboot.common.enums;

import com.github.doiteasy.easyboot.common.result.ResultCodeEnum;

public enum DefaultResultCodeEnum implements ResultCodeEnum {


    SUCCESS(200, "成功"),
    BAD_REQUEST(400, "请求错误"),
    UNAUTHORIZED(401, "服务未授权"),
    FORBIDDEN(403, "资源禁用"),
    NOT_FOUND(404, "服务未授权"),
    INTERNAL_SERVER_ERROR(500, "服务内部异常"),
    BAD_GATEWAY(502, "网关服务异常"),
    SERVICE_TIMEOUT(504, "服务调用超时");

    ;

    private Integer code;
    private String message;

    DefaultResultCodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }




}
