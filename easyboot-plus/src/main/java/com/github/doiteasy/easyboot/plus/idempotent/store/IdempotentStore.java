package com.github.doiteasy.easyboot.plus.idempotent.store;

public interface IdempotentStore {

    /**
     * set key and value
     *
     * @param key        key
     * @param value      value
     * @param expireTime expireTime unit: seconds
     * @return true or false
     */
    Boolean set(String key, String value, long expireTime);

    /**
     * delete key
     *
     * @param key key
     * @return true or false
     */
    Boolean delete(String key);

    /**
     * get key
     *
     * @param key key
     * @return value
     */
    String get(String key);
}

