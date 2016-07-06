/**
 * MemcacheProperties.java
 * 
 * Copyright@2016 OVT Inc. All rights reserved. 
 * 
 * Apr 7, 2016
 */
package cn.wisdom.lottery.payment.dao.cache;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * MemcacheProperties
 * 
 * @Author zhi.liu
 * @Version 1.0
 * @See com.whalin.MemCached.SockIOPool
 * @Since [OVT Cloud Platform]/[API] 1.0
 */
@Component
public class MemcacheProperties
{
    @Value("${memcache.enable}")
    private boolean enable = false;
    
    @Value("${memcache.server}")
    private String server = "";
    
    @Value("${memcache.initConn}")
    private int initConn = 3;
    
    @Value("${memcache.minConn}")
    private int minConn = 3;
    
    @Value("${memcache.maxConn}")
    private int maxConn = 100;
    
    @Value("${memcache.maintSleep}")
    private int maintSleep = 1000 * 30;
    
    @Value("${memcache.nagle}")
    private boolean nagle = false;
    
    @Value("${memcache.socketTO}")
    private int socketTO = 1000 * 3;
    
    @Value("${memcache.socketConnectTO}")
    private int socketConnectTO = 1000 * 3;

    public String getServer()
    {
        return server;
    }

    public void setServer(String server)
    {
        this.server = server;
    }

    public int getInitConn()
    {
        return initConn;
    }

    public void setInitConn(int initConn)
    {
        this.initConn = initConn;
    }

    public int getMinConn()
    {
        return minConn;
    }

    public void setMinConn(int minConn)
    {
        this.minConn = minConn;
    }

    public int getMaxConn()
    {
        return maxConn;
    }

    public void setMaxConn(int maxConn)
    {
        this.maxConn = maxConn;
    }

    public int getMaintSleep()
    {
        return maintSleep;
    }

    public void setMaintSleep(int maintSleep)
    {
        this.maintSleep = maintSleep;
    }

    public boolean isNagle()
    {
        return nagle;
    }

    public void setNagle(boolean nagle)
    {
        this.nagle = nagle;
    }

    public int getSocketTO()
    {
        return socketTO;
    }

    public void setSocketTO(int socketTO)
    {
        this.socketTO = socketTO;
    }

    public int getSocketConnectTO()
    {
        return socketConnectTO;
    }

    public void setSocketConnectTO(int socketConnectTO)
    {
        this.socketConnectTO = socketConnectTO;
    }

    public boolean isEnable()
    {
        return enable;
    }

    public void setEnable(boolean enable)
    {
        this.enable = enable;
    }
    
    
}
