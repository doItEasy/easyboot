package com.github.doiteasy.easyboot.tools.snowflake;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.UnknownHostException;

/**
 * 雪花算法唯一序号生成工具
 *
 * @version $Id: SnowSequenceHelper.java
 */
public class SnowSequenceHelper {



    private static SnowFlake instance;

    private static final Logger logger = LoggerFactory.getLogger(SnowSequenceHelper.class);

    static {
        init();
    }

    /**
     * 生成规则：16位长度雪花算法序列号
     *
     * @return
     */
    public static long nextSequence() {
        return instance.nextId();
    }

    /**
     * 生成规则：4位前缀编号 + 16位雪花算法序列号
     * <p>
     * 前缀编号:@link SnowSequenceHelper.PREFIX_*
     *
     * @param prefix 指定前缀
     * @return
     */
    public static String nextSequence(String prefix) {
        return prefix + instance.nextId();
    }

    /**
     * 初始化雪花算法序列号生成器
     *
     * @throws UnknownHostException
     */
    private static synchronized void init() {
        if (instance != null) {
            logger.info("------------- Snow Flake Sequence has initialized --------------");
            return;
        }

        try {
            String[] ipArray = GrabMachineIpUtil.getLocalHostLANAddress().getHostAddress().split("\\.");
            long addr_t = Long.valueOf(ipArray[2]).longValue();
            long addr_f = Long.valueOf(ipArray[3]).longValue();

            long machineId = getScopeId(addr_t, SnowFlake.MAX_MACHINE_NUM);
            long dataCenterId = getScopeId(addr_f, SnowFlake.MAX_DATA_CENTER_NUM);

            instance = new SnowFlake(dataCenterId, machineId);
            logger.info("------------- Snow Flake Sequence initialize success --------------, dataCenterId:{}, machineId:{}", dataCenterId, machineId);

        } catch (UnknownHostException ue) {
            logger.error("------------- Snow Flake Sequence initialize failed --------------", ue);
            throw new RuntimeException(ue);
        }
    }

    private static long getScopeId(long addr, long maxId) throws UnknownHostException {
        long r = addr;
        if (addr >= 0L && addr <= maxId) {
            return addr;
        } else {
            do {
                do {
                    r >>= 2;
                } while (r < 0L);
            } while (r > maxId);

            return r;
        }
    }



}
