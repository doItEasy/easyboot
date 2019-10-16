package com.github.doiteasy.easyboot.plus.result;

public enum ResultCode {

    SUCCESS(200,"成功"),
    PARAM_IS_BLANK(101,"参数为空"),
    PARAM_IS_INVAILD(102,"参数无效"),
    PARAM_TYPE_BIND_ERROR(103,"参数类型错误"),
    PARAM_NOT_COMPLETE(104,"参数缺失"),

    USER_NOT_LOGIN_IN(410,"用户未登陆"),
    USER_NOT_EXIST(411,"用户不存在"),
    USER_IS_FORBIDDEN(412,"用户被禁"),
    USER_LOGIN_ERROR(413,"用户名或密码错误"),
    USER_NOT_PERMISSION(414,"没有权限"),

    ;

    private Integer code;
    private String message;

    ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }




}
