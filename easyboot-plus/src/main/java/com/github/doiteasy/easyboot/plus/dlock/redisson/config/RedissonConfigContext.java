package com.github.doiteasy.easyboot.plus.dlock.redisson.config;

import com.github.doiteasy.easyboot.plus.dlock.redisson.strategy.RedissonStrategy;
import org.redisson.config.Config;

/**
 * @author feixm
 * @desc Redisson配置上下文，产出真正的Redisson的Config
 */
public class RedissonConfigContext {

    private RedissonStrategy redissonConfigStrategy = null;

    public RedissonConfigContext(RedissonStrategy _redissonConfigStrategy) {
        this.redissonConfigStrategy = _redissonConfigStrategy;
    }

    /**
     * 上下文根据构造中传入的具体策略产出真实的Redisson的Config
     * @param redissonProperties
     * @return
     */
    public Config createRedissonConfig(RedissonProperties redissonProperties) {
        return this.redissonConfigStrategy.createRedissonConfig(redissonProperties);
    }
}
