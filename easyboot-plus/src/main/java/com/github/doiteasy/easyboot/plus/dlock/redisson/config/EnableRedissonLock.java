package com.github.doiteasy.easyboot.plus.dlock.redisson.config;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author feixm
 * @desc 开启Redisson注解支持
 */
@Inherited
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(RedissonAutoConfiguration.class)
public @interface EnableRedissonLock {
}
