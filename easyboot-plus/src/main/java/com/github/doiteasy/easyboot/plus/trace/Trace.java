package com.github.doiteasy.easyboot.plus.trace;

import java.lang.annotation.*;

/**
 * 日志追踪拦截注解，【方法上】带有该注解，会被追踪记录
 * 加在类上的的协议会被方法继承
 * 加在类上的的描述会被方法拼接*
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Trace {
    /**
     * 功能描述
     *
     * @return
     */
    String desc() default "";

    /**
     * 是否打印参数
     *
     * @return
     */
    boolean traceParam() default true;

    /**
     * 是否打印返回值
     *
     * @return
     */
    boolean traceRet() default true;
}
