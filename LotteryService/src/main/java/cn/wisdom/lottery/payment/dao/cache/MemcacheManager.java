/**
 * MemcacheManager.java
 * 
 * Copyright@2016 OVT Inc. All rights reserved. 
 * 
 * Apr 7, 2016
 */
package cn.wisdom.lottery.payment.dao.cache;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.wisdom.lottery.payment.common.codec.JsonTransCoder;
import cn.wisdom.lottery.payment.common.log.Logger;
import cn.wisdom.lottery.payment.common.log.LoggerFactory;
import cn.wisdom.lottery.payment.common.utils.DataConvertUtils;
import cn.wisdom.lottery.payment.dao.constant.LoggerConstants;
import com.ovt.order.util.entity.JsonUtils;
import com.ovt.order.util.entity.OVTException;
import com.whalin.MemCached.MemCachedClient;
import com.whalin.MemCached.SockIOPool;

/**
 * MemcacheManager
 * 
 * @Author zhi.liu
 * @Version 1.0
 * @See
 * @Since [OVT Cloud Platform]/[API] 1.0
 */
@SuppressWarnings("unchecked")
@Service
public class MemcacheManager
{
    @Autowired
    private MemcacheProperties memcacheProperties;

    private MemCachedClient client;

    private static final Logger logger = LoggerFactory
            .getLogger(LoggerConstants.MEMCACHE_LOGGER);
    
    private boolean enable = false;

    @PostConstruct
    public void init()
    {
        SockIOPool pool = SockIOPool.getInstance();

        String[] server = memcacheProperties.getServer().split(",|;");
        pool.setServers(server);
        pool.setInitConn(memcacheProperties.getInitConn());
        pool.setMinConn(memcacheProperties.getMinConn());
        pool.setMaxConn(memcacheProperties.getMaxConn());
        pool.setMaintSleep(memcacheProperties.getMaintSleep());
        pool.setNagle(memcacheProperties.isNagle());
        pool.setSocketTO(memcacheProperties.getSocketTO());
        pool.setSocketConnectTO(memcacheProperties.getSocketConnectTO());

        pool.initialize();

        client = new MemCachedClient();
        client.setTransCoder(new JsonTransCoder());
        
        this.enable = memcacheProperties.isEnable();
    }

    public Object get(String key)
    {
        return enable ? client.get(key) : null;
    }

    public <T> T get(String key, Class<T> valClazz)
    {
        T instance = null;
        if (enable)
        {
            Object value = client.get(key);
            if (valClazz.isPrimitive())
            {
                return (T) value;
            }
            
            try
            {
                instance =
                        JsonUtils.fromJson(DataConvertUtils.toString(value),
                                valClazz);
            }
            catch (OVTException e)
            {
                String errMsg =
                        MessageFormat.format("Failed to convert json to {0}: {1}",
                                valClazz.getName(), value);
                logger.error(errMsg, e);
            }
        }

        return instance;
    }

    public Map<String, Object> getMulti(String... keys)
    {
        return enable ? client.getMulti(keys) : null;
    }

    public <T> Map<String, T> getMulti(Class<T> valClazz, String... keys)
    {
        Map<String, T> result = null;
        if (enable)
        {
            Map<String, Object> multi = client.getMulti(keys);
            result = new HashMap<String, T>(multi.size());
            if (valClazz.isPrimitive())
            {
                Object val;
                for (String key : multi.keySet())
                {
                    val = multi.get(key);
                    result.put(key, (T) val);
                }
                
                return result;
            }
            
            Object val = null;
            T valT;
            for (String key : multi.keySet())
            {
                try
                {
                    val = multi.get(key);
                    valT =
                            JsonUtils.fromJson(DataConvertUtils.toString(val),
                                    valClazz);
                    
                    result.put(key, valT);
                }
                catch (OVTException e)
                {
                    String errMsg =
                            MessageFormat.format(
                                    "Failed to convert json to {0}: {1}",
                                    valClazz.getName(), val);
                    logger.error(errMsg, e);
                }
            }
        }

        return result;
    }

    public void put(String key, Object value)
    {
        if (enable)
        {
            client.set(key, value);
        }
    }
    
    /**
     * Remove the key/value from cache.
     * 
     * @param key
     */
    public void evict(String key)
    {
        if (enable)
        {
            client.delete(key);
        }
    }

    public boolean isEnable()
    {
        return enable;
    }
}
