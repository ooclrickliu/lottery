/**
 * JedisManager.java
 * 
 * Copyright@2016 OVT Inc. All rights reserved. 
 * 
 * Apr 13, 2016
 */
package com.ovt.order.dao.cache;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ovt.common.utils.StringUtils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * JedisManager
 * 
 * @Author zhi.liu
 * @Version 1.0
 * @See
 * @Since [OVT Cloud Platform]/[API] 1.0
 */
@Service
public class RedisManager
{

    private static final String NULL = "null";

    private JedisPool jedisPool;

    @Autowired
    private RedisProperties jedisProperties;
    
    private boolean enable = false;

    @PostConstruct
    public void init()
    {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(jedisProperties.getMaxTotal());
        config.setMaxIdle(jedisProperties.getMaxIdle());
        config.setMinIdle(jedisProperties.getMinIdle());
        config.setTestOnBorrow(true);

        jedisPool =
                new JedisPool(config, jedisProperties.getHost(),
                        jedisProperties.getPort(),
                        jedisProperties.getSocketConnectTO(),
                        jedisProperties.getPassword());
        
        this.enable = jedisProperties.isEnable();
    }

    public Jedis getJedis()
    {
        return jedisPool.getResource();
    }

    public boolean isEnable()
    {
        return enable;
    }
    
    public static boolean isNull(String value)
    {
        return StringUtils.isBlank(value) || StringUtils.equalsIgnoreCase(value, NULL);
    }
}
