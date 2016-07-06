/**
 * JedisProperties.java
 * 
 * Copyright@2016 OVT Inc. All rights reserved. 
 * 
 * Apr 13, 2016
 */
package com.ovt.order.dao.cache;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * JedisProperties
 * 
 * @Author zhi.liu
 * @Version 1.0
 * @See 
 * @Since [OVT Cloud Platform]/[API] 1.0
 */
@Component
public class RedisProperties
{
    @Value("${redis.enable}")
    private boolean enable = false;
    
    @Value("${redis.host}")
    private String host = "localhost";
    
    @Value("${redis.port}")
    private int port = 6379;
    
    @Value("${redis.password}")
    private String password = "";
    
    @Value("${redis.maxTotal}")
    private int maxTotal = 1024;
    
    @Value("${redis.maxIdle}")
    private int maxIdle = 1024;
    
    @Value("${redis.minIdle}")
    private int minIdle = 10;
    
    @Value("${redis.socketConnectTO}")
    private int socketConnectTO = 1000 * 3;

    public boolean isEnable()
    {
        return enable;
    }

    public void setEnable(boolean enable)
    {
        this.enable = enable;
    }

    public String getHost()
    {
        return host;
    }

    public void setHost(String host)
    {
        this.host = host;
    }

    public int getPort()
    {
        return port;
    }

    public void setPort(int port)
    {
        this.port = port;
    }

    public int getMaxTotal()
    {
        return maxTotal;
    }

    public void setMaxTotal(int maxTotal)
    {
        this.maxTotal = maxTotal;
    }

    public int getMaxIdle()
    {
        return maxIdle;
    }

    public void setMaxIdle(int maxIdle)
    {
        this.maxIdle = maxIdle;
    }

    public int getMinIdle()
    {
        return minIdle;
    }

    public void setMinIdle(int minIdle)
    {
        this.minIdle = minIdle;
    }

    public int getSocketConnectTO()
    {
        return socketConnectTO;
    }

    public void setSocketConnectTO(int socketConnectTO)
    {
        this.socketConnectTO = socketConnectTO;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

}
