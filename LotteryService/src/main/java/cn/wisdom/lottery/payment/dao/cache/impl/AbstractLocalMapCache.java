/**
 * AbstractLocalMapCache.java
 * 
 * Copyright@2015 OVT Inc. All rights reserved. 
 * 
 * May 27, 2015
 */
package cn.wisdom.lottery.payment.dao.cache.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import cn.wisdom.lottery.payment.common.model.LRUMap;
import cn.wisdom.lottery.payment.common.utils.CollectionUtils;
import cn.wisdom.lottery.payment.dao.cache.LocalCacheManager;
import cn.wisdom.lottery.payment.dao.cache.MapCache;

/**
 * AbstractLocalMapCache: LRU implementation of MapCache
 * 
 * @Author zhi.liu
 * @Version 1.0
 * @See
 * @Since [OVT Cloud Platform]/[DAO] 1.0
 */
public abstract class AbstractLocalMapCache<K, V> implements MapCache<K, V>
{
    private static final int DEFAULT_INITIAL_CAPACITY = 512;
    
    private static final int DEFAULT_CAPACITY = 2000;

    private Map<K, V> map;
    
    private int maxCapacity;
    
    private String cacheName;

    @Autowired
    private LocalCacheManager localCacheManager;

    public AbstractLocalMapCache(String cacheName)
    {
        this(cacheName, DEFAULT_CAPACITY);
    }

    public AbstractLocalMapCache(String cacheName, int maxCapacity)
    {
        this.cacheName = cacheName;
        
        map = Collections.synchronizedMap(new LRUMap<K, V>(
                DEFAULT_INITIAL_CAPACITY, maxCapacity));
        this.maxCapacity = maxCapacity;
    }

    @PostConstruct
    private void register()
    {
        localCacheManager.registerCache(this.getName(), this);
    }
    
    /* (non-Javadoc)
     * @see com.ovt.dao.cache.LocalCache#getName()
     */
    public String getName()
    {
        return this.cacheName;
    }
    
    /* (non-Javadoc)
     * @see com.ovt.dao.cache.Cache#getData()
     */
    public Object getData()
    {
        return Collections.unmodifiableMap(map);
    }
    
    /* (non-Javadoc)
     * @see com.ovt.dao.cache.Cache#getSize()
     */
    public int getSize()
    {
        return map.size();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ovt.dao.cache.LocalCache#clear()
     */
    public void clear()
    {
        map.clear();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ovt.dao.cache.MapCache#put(java.lang.Object, java.lang.Object)
     */
    public V put(K key, V value)
    {
        return map.put(key, value);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ovt.dao.cache.MapCache#putAll(java.util.Map)
     */
    public void putAll(Map<K, V> map)
    {
        this.map.putAll(map);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ovt.dao.cache.MapCache#get(java.lang.Object)
     */
    public V get(K key)
    {
        return map.get(key);
    }
    
    /* (non-Javadoc)
     * @see com.ovt.dao.cache.MapCache#getAll(java.util.Collection)
     */
    public Map<K, V> getAll(Collection<K> keys)
    {
        Map<K, V> subMap = new HashMap<K, V>();
        if (CollectionUtils.isNotEmpty(keys))
        {
            for (K key : keys)
            {
                if (map.get(key) != null)
                {
                    subMap.put(key, map.get(key));
                }
            }
        }
        return subMap;
    }
    
    public Collection<V> values()
    {
        return map.values();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ovt.dao.cache.MapCache#remove(java.lang.Object)
     */
    public V remove(K key)
    {
        return map.remove(key);
    }
    
    /* (non-Javadoc)
     * @see com.ovt.dao.cache.MapCache#removeAll(java.util.Collection)
     */
    public void removeAll(Collection<K> keys)
    {
        if (CollectionUtils.isNotEmpty(keys))
        {
            for (K key : keys)
            {
                this.remove(key);
            }
        }
        
    }

    public int getMaxCapacity()
    {
        return maxCapacity;
    }
    
    /* (non-Javadoc)
     * @see cn.wisdom.lottery.payment.dao.cache.Cache#preLoad()
     */
    @Override
    public void preLoad()
    {
        
    }

}
