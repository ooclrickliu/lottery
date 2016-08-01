/**
 * MapCache.java
 * 
 * Copyright@2015 OVT Inc. All rights reserved. 
 * 
 * May 27, 2015
 */
package cn.wisdom.lottery.dao.cache;

import java.util.Collection;
import java.util.Map;

/**
 * MapCache
 * 
 * @Author zhi.liu
 * @Version 1.0
 * @See 
 * @Since [OVT Cloud Platform]/[DAO] 1.0
 */
public interface MapCache<K,V> extends Cache
{

    V put(K key, V value);
    
    void putAll(Map<K, V> map);
    
    V get(K key);
    
    Map<K, V> getAll(Collection<K> keys);
    
    V remove(K key);

    void removeAll(Collection<K> keys);
}
