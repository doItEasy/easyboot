package com.github.doiteasy.easyboot.tools.snowflake;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class SnowflakeUtil {
	private static final Logger logger = LoggerFactory.getLogger(SnowflakeUtil.class);
	private static InetAddress inetAddress;
	private static long addr_t;
	private static long addr_f;
    private static UnknownHostException ex=null;
	static {
		try {
			String[] ipArray = GrabMachineIpUtil.getLocalHostLANAddress().getHostAddress().split("\\.");
			addr_t = Long.valueOf(ipArray[2]);
			addr_f = Long.valueOf(ipArray[3]);
		} catch (UnknownHostException e) {
			logger.error("init ", e);
			ex=e;
		}
	}

	/**
	 * 获取工作机器ID(0-31)
	 *
	 * @return
	 * @throws UnknownHostException
	 */
	public static long getWorkerId() throws UnknownHostException {
		return getIdScope(addr_t);
	}

	/**
	 * 获取工作机器ID(0-31)
	 *
	 * @return
	 * @throws UnknownHostException
	 */
	public static long getDatacenterId() throws UnknownHostException {
		return getIdScope(addr_f);
	}

	private static long getIdScope(long addr) throws UnknownHostException  {
		if(null!=ex)
		{
			throw ex;
		}
		long r = addr;
		if (r >= 0 && r <= 31) {
			return r;
		} else {
			while (true) {
				r = r >> 2;
				if (r >= 0 && r <= 31)
					break;
			}
			return r;
		}
	}
}
