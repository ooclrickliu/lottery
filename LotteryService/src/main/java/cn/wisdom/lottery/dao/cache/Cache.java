/**
 * Cache.java
 * 
 * Copyright@2015 OVT Inc. All rights reserved. 
 * 
 * May 27, 2015
 */
package cn.wisdom.lottery.dao.cache;

/**
 * Cache
 * 
 * @Author zhi.liu
 * @Version 1.0
 * @See 
 * @Since [OVT Cloud Platform]/[DAO] 1.0
 */
public interface Cache
{

    /**
     * Cache name.
     * 
     * @return
     */
    String getName();
    
    /**
     * Get all data stored in the cache. This is an internal API.
     * 
     * @return
     */
    Object getData();
    
    /**
     * Get cached data number.
     * 
     * @return
     */
    int getSize();
    
    /**
     * Pre-load data.
     */
    void preLoad();
    
    /**
     * Clear all data.
     */
    void clear();
    
}
