package com.github.doiteasy.easyboot.plus.dlock.redisson.strategy;

import com.github.doiteasy.easyboot.plus.dlock.redisson.config.RedissonProperties;
import org.apache.commons.lang3.StringUtils;
import org.redisson.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author feixm
 * @desc 主从方式Redisson配置
 * 连接方式：主节点,子节点,子节点
 *          格式为: 127.0.0.1:6379,127.0.0.1:6380,127.0.0.1:6381
 */
public class MasterslaveRedissonStrategy implements RedissonStrategy {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClusterRedissonStrategy.class);

    @Override
    public Config createRedissonConfig(RedissonProperties redissonProperties) {
        Config config = new Config();
        try {
            String address = redissonProperties.getAddress();
            String password = redissonProperties.getPassword();
            int database = redissonProperties.getDatabase();
            String[] addrTokens = address.split(",");
            String masterNodeAddr = addrTokens[0];
            /**设置主节点ip*/
            config.useMasterSlaveServers().setMasterAddress(masterNodeAddr);
            if (StringUtils.isNotBlank(password)) {
                config.useMasterSlaveServers().setPassword(password);
            }
            config.useMasterSlaveServers().setDatabase(database);
            /**设置从节点，移除第一个节点，默认第一个为主节点*/
            List<String> slaveList = new ArrayList<>();
            for (String addrToken : addrTokens) {
                slaveList.add(REDIS_CONNECTION_PREFIX + addrToken);
            }
            slaveList.remove(0);

            config.useMasterSlaveServers().addSlaveAddress((String[]) slaveList.toArray());
            LOGGER.info("初始化[MASTERSLAVE]方式Config,redisAddress:" + address);
        } catch (Exception e) {
            LOGGER.error("MASTERSLAVE Redisson init error", e);
            e.printStackTrace();
        }
        return config;
    }

}
