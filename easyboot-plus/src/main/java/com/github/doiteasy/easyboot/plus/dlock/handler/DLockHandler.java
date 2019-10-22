package com.github.doiteasy.easyboot.plus.dlock.handler;

import com.github.doiteasy.easyboot.plus.dlock.annotation.DLock;
import com.github.doiteasy.easyboot.plus.dlock.redisson.RedissonLock;
import com.github.doiteasy.easyboot.plus.dlock.redisson.RedissonLock;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author feixm
 * @desc Redisson分布式锁注解解析器
 */
@Aspect
@Component
public class DLockHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(DLockHandler.class);

    @Autowired
    RedissonLock redissonLock;

    @Around("@annotation(dLock)")
    public void around(ProceedingJoinPoint joinPoint, DLock dLock) {
        LOGGER.info("[开始]执行RedisLock环绕通知,获取Redis分布式锁开始");

        String lockName = dLock.value();
        int expireSeconds = dLock.expireSeconds();

        if (redissonLock.lock(lockName, expireSeconds)) {
            try {
                LOGGER.info("获取Redis分布式锁[成功]，加锁完成，开始执行业务逻辑...");
                joinPoint.proceed();
            } catch (Throwable throwable) {
                LOGGER.error("获取Redis分布式锁[异常]，加锁失败", throwable);
                throwable.printStackTrace();
            } finally {
                redissonLock.release(lockName);
            }
            LOGGER.info("释放Redis分布式锁[成功]，解锁完成，结束业务逻辑...");
        } else {
            LOGGER.error("获取Redis分布式锁[失败]");
        }
        LOGGER.info("[结束]执行RedisLock环绕通知");
    }
}
