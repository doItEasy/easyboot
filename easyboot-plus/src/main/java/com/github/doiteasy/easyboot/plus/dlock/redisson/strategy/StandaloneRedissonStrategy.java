package com.github.doiteasy.easyboot.plus.dlock.redisson.strategy;

import com.github.doiteasy.easyboot.plus.dlock.redisson.config.RedissonProperties;
import org.apache.commons.lang3.StringUtils;
import org.redisson.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author feixm
 * @desc 单机方式Redisson配置
 */
public class StandaloneRedissonStrategy implements RedissonStrategy {

    private static final Logger LOGGER = LoggerFactory.getLogger(StandaloneRedissonStrategy.class);

    @Override
    public Config createRedissonConfig(RedissonProperties redissonProperties) {
        Config config = new Config();
        try {
            String address = redissonProperties.getAddress();
            String password = redissonProperties.getPassword();
            int database = redissonProperties.getDatabase();
            String redisAddr = REDIS_CONNECTION_PREFIX + address;
            config.useSingleServer().setAddress(redisAddr);
            config.useSingleServer().setDatabase(database);
            if (StringUtils.isNotBlank(password)) {
                config.useSingleServer().setPassword(password);
            }
            LOGGER.info("初始化[standalone]方式Config,redisAddress:" + address);
        } catch (Exception e) {
            LOGGER.error("standalone Redisson init error", e);
            e.printStackTrace();
        }
        return config;
    }
}
