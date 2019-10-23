package com.github.doiteasy.easyboot.plus.idempotent.store;

import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;

public class RedisStore implements IdempotentStore{

    @Resource
    private RedisTemplate<String, String> redisTemplate;


    @Override
    public Boolean set(String key, String value, long expireTime) {
        return null;
    }

    @Override
    public Boolean delete(String key) {
        return null;
    }

    @Override
    public String get(String key) {
        return null;
    }
}
