package com.github.doiteasy.easyboot.plus.dlock.annotation;

import java.lang.annotation.*;

/**
 * @author feixm
 * @desc Redisson分布式锁注解
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface DLock {


    /**分布式锁名称*/
    String value();

    /**锁超时时间,默认十秒*/
    int expireSeconds() default 10;
}


