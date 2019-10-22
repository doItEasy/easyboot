package com.github.doiteasy.easyboot.plus.dlock.redisson.strategy;

import com.github.doiteasy.easyboot.plus.dlock.redisson.config.RedissonProperties;
import org.redisson.config.Config;


public interface RedissonStrategy {

    /**
     * 根据不同的Redis配置策略创建对应的Config
     * @param redissonProperties
     * @return Config
     */
    Config createRedissonConfig(RedissonProperties redissonProperties);



    /**Redis地址配置前缀*/
    String REDIS_CONNECTION_PREFIX ="redis://";
}
