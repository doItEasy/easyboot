package com.github.doiteasy.easyboot.common.result;

import com.github.doiteasy.easyboot.common.enums.DefaultResultCodeEnum;
import lombok.Data;

import java.io.Serializable;

@Data
public class Result implements Serializable {

    private Integer code;
    private String message;
    private Object data;

    public Result(ResultCodeEnum resultCode) {
        this.code = resultCode.getCode();
        this.message = resultCode.getMessage();
    }

    public Result(ResultCodeEnum resultCode, Object data) {
        this.code = resultCode.getCode();
        this.message = resultCode.getMessage();
        this.data = data;
    }

    public Result(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Result(Integer code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static Result success(){
        return new Result(DefaultResultCodeEnum.SUCCESS);
    }

    public static Result success(Object data){
        return new Result(DefaultResultCodeEnum.SUCCESS,data);
    }

    public static Result failure(ResultCodeEnum resultCode){
        return new Result(resultCode);
    }

    public static Result failure(ResultCodeEnum resultCode, Object data){
        return new Result(resultCode,data);
    }

    public static Result failure(Integer code, String message){
        return new Result(code,message);
    }

    public static Result failure(Integer code, String message, Object data){
        return new Result(code,message,data);
    }

}
