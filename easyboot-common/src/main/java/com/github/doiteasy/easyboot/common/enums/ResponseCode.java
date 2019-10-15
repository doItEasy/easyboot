package com.github.doiteasy.easyboot.common.enums;

public enum ResponseCode {

    SUCCESS(200, "成功", false),
    BAD_REQUEST(400, "请求错误", false),
    UNAUTHORIZED(401, "服务未授权", false),
    INTERNAL_SERVER_ERROR(500, "服务内部异常", true),
    BAD_GATEWAY(502, "网关服务异常", true),
    SERVICE_TIMEOUT(504, "服务调用超时", true),
    ;

    private int code;
    private String desc;
    private boolean retriable;

    ResponseCode(int code, String desc, boolean retriable) {
        this.code = code;
        this.desc = desc;
        this.retriable = retriable;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public boolean isRetriable() {
        return retriable;
    }
}
