package com.github.doiteasy.easyboot.plus.result;


import java.lang.annotation.*;


@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD,ElementType.TYPE})
@Documented
public @interface ResponseResult {
}
